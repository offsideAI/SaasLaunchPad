#!/bin/bash

# Print debugging info to stderr (will show in Claude logs)
echo "Starting weather MCP server" >&2

# Export full PATH to include where uv is installed
export PATH="/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:$HOME/.local/bin:$HOME/Library/Python/3.9/bin"

# Print PATH for debugging
echo "----------------"
echo "PATH: $PATH" >&2
echo "----------------"

# Print which uv for debugging
which uv >&2

# Change to the weather directory
cd "/Users/coder/repos/offsideAI/githubrepos/SaasLaunchPad/saaslaunchpad-mcp-server-weather/weather/"

# Use uv to run the Python script
uv run weather.py