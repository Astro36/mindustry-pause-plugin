# Mindustry Pause Plugin

> [Mindustry](https://mindustrygame.github.io/) Server Plugin: Enable the Game Pause on the Multiplay Server

> **Note: This plugin is deprecated since [Mindustry v105(6.0 Build 105)](https://github.com/Anuken/Mindustry/commit/88608b3f889d4ec2cfe54f1561f64f7f89de300a).**
>
> **Use official server pause command instead: `$ pause <on/off>`**

## ChangeLog

See [CHANGELOG](./CHANGELOG.md)

## Features

- Add `/pause` and `/resume`.
- Auto pause the game when server is empty.

## Installation

Place the [plugin(jar)](https://github.com/Astro36/mindustry-pause-plugin/releases/latest) file in the server's `config/mods` directory and restart the server.

## Usages

### Client Command

| Command  | Description      |
| -------- | ---------------- |
| pause    | Pause the game.  |
| resume   | Resume the game. |

### Server Command

| Command  | Description      |
| -------- | ---------------- |
| pause    | Pause the game.  |
| resume   | Resume the game. |

### Config

Server's `config/mods/pause.json` file:

```json
{
    "pauseAuto": true,
    "pausePermission": "ADMIN_ONLY"
}
```

- `pauseAuto`: If `pauseAuto` is true, the game will pause automatically when no one is in the world.
- `pausePermission`: Set the user's level to use the command. (`ALL` or `ADMIN_ONLY`)

## License

```text
Copyright (c) 2020 Seungjae Park

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

Mindustry Pause Plugin is licensed under the [MIT License](./LICENSE).
