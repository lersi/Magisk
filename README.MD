![](docs/images/logo.png)

[![Downloads](https://img.shields.io/badge/dynamic/json?color=green&label=Downloads&query=totalString&url=https%3A%2F%2Fraw.githubusercontent.com%2Ftopjohnwu%2Fliorsmagic-files%2Fcount%2Fcount.json&cacheSeconds=1800)](https://raw.githubusercontent.com/topjohnwu/liorsmagic-files/count/count.json)

#### This is not an officially supported Google product

## Introduction

Liorsmagic is a suite of open source software for customizing Android, supporting devices higher than Android 6.0.<br>
Some highlight features:

- **LiorsmagicSU**: Provide root access for applications
- **Liorsmagic Modules**: Modify read-only partitions by installing modules
- **LiorsmagicBoot**: The most complete tool for unpacking and repacking Android boot images
- **Zygisk**: Run code in every Android applications' processes

## Downloads

[Github](https://github.com/topjohnwu/Liorsmagic/) is the only source where you can get official Liorsmagic information and downloads.

[![](https://img.shields.io/badge/Liorsmagic-v26.1-blue)](https://github.com/topjohnwu/Liorsmagic/releases/tag/v26.1)
[![](https://img.shields.io/badge/Liorsmagic%20Beta-v26.1-blue)](https://github.com/topjohnwu/Liorsmagic/releases/tag/v26.1)
[![](https://img.shields.io/badge/Liorsmagic-Canary-red)](https://raw.githubusercontent.com/topjohnwu/liorsmagic-files/canary/app-release.apk)
[![](https://img.shields.io/badge/Liorsmagic-Debug-red)](https://raw.githubusercontent.com/topjohnwu/liorsmagic-files/canary/app-debug.apk)

## Useful Links

- [Installation Instruction](https://topjohnwu.github.io/Liorsmagic/install.html)
- [Building and Development](https://topjohnwu.github.io/Liorsmagic/build.html)
- [Liorsmagic Documentation](https://topjohnwu.github.io/Liorsmagic/)

## Bug Reports

**Only bug reports from Debug builds will be accepted.**

For installation issues, upload both boot image and install logs.<br>
For Liorsmagic issues, upload boot logcat or dmesg.<br>
For Liorsmagic app crashes, record and upload the logcat when the crash occurs.

## Translation Contributions

Default string resources for the Liorsmagic app and its stub APK are located here:

- `app/src/main/res/values/strings.xml`
- `stub/src/main/res/values/strings.xml`

Translate each and place them in the respective locations (`[module]/src/main/res/values-[lang]/strings.xml`).

## License

    Liorsmagic, including all git submodules are free software:
    you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
