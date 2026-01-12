using Newtonsoft.Json;

public class RequestFactory
{
    public async Task sendRequestAsync(StreamWriter writer, string type, object param, string requestId)
    {
        var request = new
        {
            MessageType = "Request",
            RequestId = requestId,
            Type = type,
            Param = param
        };

        string json = JsonConvert.SerializeObject(request);
        await writer.WriteLineAsync(json);
    }
}