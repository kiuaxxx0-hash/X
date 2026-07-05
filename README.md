# X Launcher 🚀

**X Launcher** is a cutting-edge, high-performance, and feature-rich open-source Minecraft Java Edition launcher for Android devices. Built entirely from scratch with modern software architecture patterns, X Launcher aims to deliver desktop-class stability, automated mod management, and advanced layout virtualization directly to mobile platforms.

---

## ✨ Features Checklist & Roadmap

- [x] **Modern Fluid UI Layout**: Horizon landscape design built entirely via sharp XML vectors with elegant cream and dark aesthetic layouts.
- [x] **Smart Version Selector**: Integrated client manifest controller supporting multi-version lifecycle management (1.16.5, 1.20.1, 1.21+).
- [x] **X Central Mods Hub**: Automated networking interface connected directly to live **Modrinth** and **CurseForge** web APIs.
- [x] **Dependency Auto-Resolver**: Dynamic installation engine that auto-resolves, fetches, and injects required sub-mod dependencies cleanly.
- [x] **Integrated File Manager**: On-board local storage filesystem explorer to customize game assets, saves, and configs inside the launcher.
- [x] **Advanced JVM Tweak Panel**: Complete control over low-level runtime configurations, including dynamic smart RAM heap boundary allocations.
- [x] **Virtual Touch Mapping Editor**: Visual canvas controller supporting fluid size scaling, alpha opacity shifts, and positioning coordinates for virtual on-screen keys.
- [x] **Secure Session Profiler**: Shared user authentication local cache memory supporting offline identifiers and prepared for secure premium Microsoft/GitHub OAuth integrations.

---

## 🛠️ Architecture & Tech Stack

X Launcher is built utilizing robust industry-standard Android development components to ensure continuous stability:
- **Programming Language**: 100% Kotlin (Modern Object-Oriented Sub-routines)
- **UI Framework**: Vector Drawables & Material Design 3 XML layouts
- **Asynchronous Execution Pipeline**: Kotlin Coroutines & Lifecycle Scopes
- **Networking Adapters**: Retrofit 2 & GSON Converters for API Streams
- **Data Binder Subsystems**: ViewBinding for optimized view tree indexing
- **Persistence Storage Layers**: Android Context SharedPreferences

---

## 📂 Core Repository Structure

```text
X-Launcher/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml      # Global app capabilities & landscape orientations
│   │   ├── java/com/x/launcher/
│   │   │   ├── auth/                # Session handling, encryption & AccountManagers
│   │   │   ├── engine/              # Process Builders, JVM boot streams & ModInstallers
│   │   │   ├── models/              # Rigid network data payload schemas
│   │   │   ├── network/             # Retrofit service interfaces & UI Adapters
│   │   │   └── *Activities.kt       # View lifecycle coordination files
│   │   └── res/
│   │       ├── layout/              # Full XML structural UI view maps
│   │       └── drawable/            # Smooth programmatic corner shapes & Vector Assets
│   └── build.gradle.kts             # Dependency injector & build specifications
└── build.gradle.kts                 # Main root plugins catalog definitions
```

---

## 🤝 Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**. 

Feel free to fork the repository, open issues regarding feature extensions, or submit Pull Requests!

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.
