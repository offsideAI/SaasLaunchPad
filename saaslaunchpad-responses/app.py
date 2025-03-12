from openai import OpenAI
client = OpenAI()

response = client.responses.create(
    model="gpt-4o",
    input="Write a product spec for a supply-chain ERP logistics management solution"
)

print(response.output_text)