from openai import OpenAI
import gradio as gr
import os

def generate_response(api_key, prompt):
    # Create a new client with the provided API key
    client = OpenAI(api_key=api_key)

    try:
        response = client.responses.create(
            model="gpt-4o",
            tools=[{
                "type": "file_search",
                "vector_store_ids": "<vector_store_id"
            }],
            input=prompt
        )
        return response.output_text
    except Exception as e:
        return f"Error: {str(e)}"

# Create Gradio interface
demo = gr.Interface(
    fn=generate_response,
    inputs=[
        gr.Textbox(
            placeholder="Enter your OpenAI API key here...",
            type="password",
            label="OpenAI API Key",
            value=os.environ.get("OPENAI_API_KEY", "")
        ),
        gr.Textbox(
            lines=5,
            placeholder="Enter your prompt here...",
            label="Prompt"
        )
    ],
    outputs=gr.Textbox(lines=20, label="Generated Response"),
    title="SaaS Launch Pad",
    description="Generate product specs, documentation, and more using OpenAI's Responses API",
    examples=[
        [None, "Write a product spec for a supply-chain ERP logistics management solution"],
        [None, "Create a marketing plan for a new fitness app"],
        [None, "Design a user onboarding flow for a SaaS platform"]
    ]
)

if __name__ == "__main__":
    demo.launch()
