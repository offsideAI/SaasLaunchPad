import { McpServer } from "@modelcontextprotocol/sdk/server/mcp.js"
import { StdioServerTransport } from "@modelcontextprotocol/sdk/server/stdio.js"
import { z } from "zod";


const server = new McpServer({
    name: "SaasLaunchPad Service",
    version: "1.0.0",
});

server.tool('getWeather', {
    city: z.string()
},
async ({ city }) => {
    return {
        content: [
            {
                type: "text",
                text: `The weather in ${city} is cloudy!`,
            },
        ],
    };
  },
);



