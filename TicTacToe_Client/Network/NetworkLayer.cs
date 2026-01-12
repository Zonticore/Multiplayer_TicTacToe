using System.Collections.Concurrent;
using System.Net.Sockets;
using System.Text;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

class NetworkLayer
{

    //Sends requests recieves responses, and recieves server events

    public event Action<string, JObject>? OnEventReceived;

    public NetworkLayer()
    {
        requestFactory = new RequestFactory();
        _pendingResponses = new ConcurrentDictionary<string, TaskCompletionSource<Response>>();
        _listenerCts = new CancellationTokenSource();
    }

    public async Task RunAsync()
    {
        Console.WriteLine($"Trying to connect to {ServerHost}:{ServerPort}...");

        try
        {
            client = new TcpClient();
            await client.ConnectAsync(ServerHost, ServerPort);
            var stream = client.GetStream();
            reader = new StreamReader(stream, new UTF8Encoding(false));
            writer = new StreamWriter(stream, new UTF8Encoding(false)) { AutoFlush = true };
            Console.WriteLine("Connected successfully!");

            _ = listenLoop(_listenerCts.Token);
        }
        catch (SocketException ex)
        {
            Console.WriteLine($"Connection failed: {ex.Message} (Is server running?)");
            throw;
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Error: {ex.Message}");
            throw;
        }
    }

    private async Task listenLoop(CancellationToken ct)
    {
        while (!ct.IsCancellationRequested)
        {
            try
            {
                if (reader == null)
                {
                    throw new InvalidOperationException("Not connected.");
                }
                string? line = await reader.ReadLineAsync(ct);
                if (line == null)
                {
                    Console.WriteLine("Connection closed by server.");
                    break;
                }

                JObject obj = JObject.Parse(line);
                string? msgType = obj["MessageType"]?.ToString();

                if (msgType == "Response")
                {
                    string? rid = obj["RequestId"]?.ToString();
                    if (rid != null && _pendingResponses.TryRemove(rid, out var tcs))
                    {
                        Response? resp = obj.ToObject<Response>();
                        tcs.SetResult(resp!);
                    }
                    else
                    {
                        Console.WriteLine("Received orphan response (no matching request).");
                    }
                }
                else if (msgType == "Event")
                {
                    handleEvent(obj);
                }
                else
                {
                    Console.WriteLine("Unknown message type received.");
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Listener error: {ex.Message}");
                break;
            }
        }
        
        foreach (var tcs in _pendingResponses.Values)
        {
            tcs.TrySetException(new IOException("Connection closed."));
        }
        _pendingResponses.Clear();
    }

    private void handleEvent(JObject obj)
    {
        string? eventType = obj["EventType"]?.ToString();
        JObject data = obj["Data"] as JObject ?? new JObject();
        OnEventReceived?.Invoke(eventType ?? "Unknown", data);
    }

    public async Task<Response?> sendRequestAsync(string type, object param)
    {
        if (writer == null)
        {
            throw new InvalidOperationException("Not connected. Call RunAsync first.");
        }

        string requestId = Guid.NewGuid().ToString();
        var tcs = new TaskCompletionSource<Response>();
        _pendingResponses[requestId] = tcs;

        try
        {
            await requestFactory.sendRequestAsync(writer, type, param, requestId);
            Console.WriteLine("Request sent, waiting for response...");
        }
        catch (Exception ex)
        {
            _pendingResponses.TryRemove(requestId, out _);
            Console.WriteLine($"Send error: {ex.Message}");
            throw;
        }

        Response? response = null;
        try
        {
            response = await tcs.Task;
        }
        catch (Exception ex)
        {
            Console.WriteLine($"Response wait error: {ex.Message}");
        }
        return response;
    }

    public async Task sendEventAsync(string eventType, object? data = null)
    {
        if (writer == null)
        {
            throw new InvalidOperationException("Not connected. Call RunAsync first.");
        }

        var eventMsg = new
        {
            MessageType = "Event",
            EventType = eventType,
            Data = data
        };

        string json = JsonConvert.SerializeObject(eventMsg);
        await writer.WriteLineAsync(json);
    }

    private TcpClient? client;
    private StreamReader? reader;
    private StreamWriter? writer;
    private RequestFactory requestFactory;
    private readonly ConcurrentDictionary<string, TaskCompletionSource<Response>> _pendingResponses;
    private CancellationTokenSource _listenerCts;

    private const string ServerHost = "127.0.0.1";
    private const int ServerPort = 5555;
}