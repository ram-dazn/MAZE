# 🧩 Maze Solver Android App

An Android application that fetches maze images, parses them, solves them using a graph traversal algorithm,
and overlays the solution on the original image. This app is built as a technical assignment using modern Android architecture principles and technologies.

## 🚀 Features

- Fetches maze data from a remote API.
- Decodes bitmap images to identify walls, start (red), and end (blue) points.
- Solves the maze using **Breadth-First Search (BFS)** with performance optimizations.
- Overlays the solution path in green on the maze.
- Built using **Jetpack Compose** for a modern UI experience.
- Clean Architecture separation: Presentation, Domain, and Data layers.
- Parallel image parsing using coroutines for better performance.
- Full unit test coverage for ViewModels, UseCases, and Repositories.

---

## 🧱 Architecture

The project follows **Clean Architecture** with the following layer responsibilities:
com.maze.mobile
│
├── domain // Interfaces, UseCases, business models
├── data // Retrofit API, Repository implementation, parsers
├── presentation // UI in Jetpack Compose, ViewModels
├── di // Hilt modules for DI setup
└── utils // Point, Maze parsing helpers, etc.

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: Clean Architecture (Domain, Data, Presentation)
- **Dependency Injection**: Hilt
- **Networking**: Retrofit
- **Image Loading**: Coil
- **Coroutines**: Kotlin Coroutines + Flows
- **Testing**: JUnit, MockK, Turbine
- **Bitmap Manipulation**: Android Bitmap APIs

---

## 🧠 Maze Solving Strategy

- The maze is parsed by scanning pixel colors:
    - **Black** → Wall
    - **White** → Path
    - **Red** → Start
    - **Blue** → End
- The solving algorithm is a **BFS** that:
    - Uses a queue and visited matrix.
    - Backtracks using a `cameFrom` array to build the final path.
- Parsed and solved on background threads for performance.
- Large images are downscaled intelligently while preserving critical color data.

---

## 🧪 Testing

- ✅ ViewModels tested with `StateFlow`, `runTest`, and assertions
- ✅ UseCases tested for success and failure paths
- ✅ Repositories tested using mocked APIs and bitmap streams
- ✅ Parser tested for start/end detection and accurate maze grid generation

---

## 📝 Assignment Purpose

This project was built for a technical interview assignment. It showcases:

- Ability to build apps using modern Android practices
- Bitmap manipulation and graphics-level problem solving
- Composable UI development with theme support
- Clean code and modular architecture
- Unit testing of all key components

---