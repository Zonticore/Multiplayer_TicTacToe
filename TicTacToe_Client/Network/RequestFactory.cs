using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;

public class RequestFactory
{
    private readonly JsonSerializerSettings _jsonSettings = new JsonSerializerSettings
    {
        ContractResolver = new CamelCasePropertyNamesContractResolver(),
        NullValueHandling = NullValueHandling.Ignore,
        Formatting = Formatting.None
    };

    public async Task SendRequestAsync(StreamWriter writer, string type, object param, string requestId)
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