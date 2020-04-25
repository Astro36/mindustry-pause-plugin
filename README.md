# Mindustry Plugin Kotlin

> [Mindustry](https://mindustrygame.github.io/) Server Plugin Template written by [Kotlin](https://kotlinlang.org/)

This template was inspired by [Anuken/ExamplePlugin](https://github.com/Anuken/ExamplePlugin).

## Setup

1. Clone the repo:

    ```bash
    $ git clone https://github.com/Astro36/mindustry-plugin-kt.git
    ```

2. Set your plugin information at `src/main/resources/plugin.json`:

    ```json
    {
        "name": "Your Plugin Name",
        "displayName": "Your Plugin Display Name (Default=name)",
        "author": "Developer Name",
        "description": "Your Plugin Description",
        "version": "1.0",
        "main": "ExamplePlugin",
        "minGameVersion": null
    }
    ```

    `minGameVersion` must be convertible to integer.

3. Delete original `LICENSE` file.
4. Check `src/main/kotlin/ExamplePlugin.kt` for some basic commands and event handlers.

## Build

```bash
$ ./gradlew jar
```

The created jar file is located in `build/libs` directory.

## Apply Plugin

Place the created jar file in the server's `config/mods` directory and restart the server.

You can check the currently installed plugins/mods by running the `mods` command.

## License

```text
MIT License

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

Mindustry Plugin Kotlin Template is licensed under the [MIT License](./LICENSE).
