# **15-0: The Build-A-Fighter Gauntlet**

A desktop UFC-inspired build-a-fighter and gauntlet simulation game developed for CSC207. Players configure a run, build a custom fighter by drafting six attributes from real UFC fighters, finalize the fighter, simulate a 15-opponent ranked gauntlet, save completed fighters, run exhibition matches, and browse the fighter catalogue.

Real UFC fighter and ranking data can be loaded through the Cito API. If the API is unavailable or no API key is configured, the application falls back to local data so the core game can still run.

## Authors and Contributors

**Team 5: Octagon**

- Hongli Tao — User Story 1: Game Settings
- John Shi — User Story 2: Fighter Creation
- Chetanya Moudgil — User Story 3: Confirm / Finalize Fighter
- Mahin Mia — User Story 4: Gauntlet Simulation
- Mohit Bendale — User Story 5: Saved Fighters / Exhibition
- Team contribution — User Story 6: Fighter Browser and project integration

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [How the Game Works](#how-the-game-works)
- [Architecture and Design](#architecture-and-design)
- [Requirements](#requirements)
- [Installation](#installation)
- [Cito API Setup](#cito-api-setup)
- [Running the Program](#running-the-program)
- [Usage Guide](#usage-guide)
- [Testing](#testing)
- [Project Structure](#project-structure)
- [Accessibility](#accessibility)
- [Common Issues](#common-issues)
- [Data, Privacy, and Local Files](#data-privacy-and-local-files)
- [Feedback](#feedback)
- [Contributing](#contributing)
- [License](#license)

## Project Overview

The goal of **15-0** is to let a player create a custom MMA fighter using the strengths of real UFC fighters and then test that build against a division's ranked contenders.
The project was created to turn real UFC fighter data into an interactive drafting and simulation game while applying the software-design principles taught in CSC207.

A run begins with configurable rules such as difficulty, number of rounds, UFC era, and whether opponent ratings should be hidden. The player then drafts the following six gameplay attributes:

- Striking
- Defense
- Takedown
- Height
- Reach
- Cardio

Each attribute is taken from a real fighter selected by the game. Once the custom fighter is complete, the player chooses a name and receives a weight class. The game then creates a `GameRun` containing the completed fighter, run settings, division, and ranked opponents.

The gauntlet is fought from rank **#15 to #1**. The user can simulate one fight at a time or auto-simulate the remaining fights. Wins and losses are both recorded, and the run continues until all 15 opponents have been fought.

## Features

### User Story 1 — Game Settings

- Select Easy, Normal, or Hard difficulty.
- Select 1, 3, or 5 rounds per fight.
- Select a UFC era to control which fighters are eligible during fighter creation.
- Choose whether opponent ratings are hidden during the gauntlet.
- Create the blank custom fighter used by the rest of the run.

### User Story 2 — Fighter Creation

- Spin for a real UFC fighter.
- View that fighter's six gameplay ratings.
- Assign one available attribute to the custom fighter.
- Use difficulty-dependent rerolls.
- Track drafted attributes and source fighters.
- Complete all six custom-fighter attributes before moving on.

### User Story 3 — Confirm / Finalize Fighter

- Review the six drafted attributes.
- Give the custom fighter a name.
- Spin and lock a weight class.
- Calculate an overall rating using division-specific attribute weightings.
- Finalize the fighter and create the gauntlet run.

### User Story 4 — Gauntlet Simulation

- Fight ranked opponents from #15 through #1.
- Simulate one fight at a time or auto-simulate the remainder.
- Generate KO/TKO, submission, or decision results.
- Record fight round and time.
- Track wins, losses, fight history, and the next opponent.
- Continue the gauntlet even after a loss.
- Respect difficulty, round count, and hidden-opponent-stat settings.

### User Story 5 — Saved Fighters and Exhibition

- Save completed custom fighters to a persistent local roster.
- View saved fighters and top performers.
- Load or delete saved fighters.
- Run one-off exhibition matches between saved fighters.
- Preserve saved fighters between program launches.

### User Story 6 — Fighter Browser

- Browse the available UFC fighter catalogue.
- Search fighters by name.
- Filter by weight class.
- Filter by UFC era.
- View professional record, ranking, and all six gameplay attributes.

### External UFC Data

When configured, the program uses the Cito API for:

- Fighter directory data.
- Fighter profiles and statistics.
- Division rankings.

The core game remains usable with local fallback data when live API data is unavailable.

## How the Game Works

A normal run follows this sequence:

```text
Welcome
  ↓
Game Settings
  ↓
Build Custom Fighter
  ↓
Confirm / Finalize Fighter
  ↓
Create GameRun
  ↓
Fight #15 → #14 → ... → #1
  ↓
Save Completed Fighter
  ↓
Saved Fighters / Exhibition
```

The Fighter Browser can be opened separately from the home screen.

## Architecture and Design

The project follows **Clean Architecture**. Business logic is kept separate from Swing, HTTP requests, JSON persistence, and other implementation details.

A typical use-case flow is:

```text
View
  ↓
Controller
  ↓
Input Data
  ↓
Input Boundary
  ↓
Interactor
  ↓
Entities / Data-Access Interfaces
  ↓
Output Data
  ↓
Output Boundary
  ↓
Presenter
  ↓
ViewModel
  ↓
View
```

### Clean Architecture Layers

- `entity` — core domain models and game rules such as `Fighter`, `CustomFighter`, `GameRun`, `FightResult`, and `FightSimulator`.
- `use_case` — application-specific actions such as configuring a run, spinning a fighter, simulating a fight, saving a fighter, and browsing fighters.
- `interface_adapter` — controllers, presenters, states, and view models that translate between the use cases and UI.
- `data_access` — implementations for Cito HTTP access, local JSON persistence, in-memory state, caching, and randomness.
- `view` — Swing user-interface classes.
- `app` — dependency wiring and application startup.

### SOLID Principles

The codebase uses SOLID principles throughout. Examples include:

- **Single Responsibility:** simulation, presentation, persistence, and UI are handled by separate classes.
- **Open/Closed:** `FightSimulator` allows different fight-simulation strategies without modifying the simulation interactor.
- **Liskov Substitution:** implementations such as `WeightedFightSimulator` can be used anywhere a `FightSimulator` is expected.
- **Interface Segregation:** saved-fighter operations use separate data-access interfaces for saving, loading, viewing, deleting, and exhibition matches.
- **Dependency Inversion:** interactors depend on interfaces such as `SimulationDataAccessInterface` and `FighterDataAccessInterface`, not concrete data-access classes.

### Design Patterns

Notable patterns include:

- **Strategy Pattern** — `FightSimulator` with `WeightedFightSimulator`.
- **Observer Pattern** — ViewModels notify Swing views through property-change events.
- **Adapter Pattern** — the Fighter Browser uses an adapter around the shared fighter data source.
- **Factory / Dependency Injection** — use-case factory classes construct controllers, interactors, presenters, and views with their dependencies.
- **DAO / Repository-style abstraction** — use cases access data through interfaces rather than directly reading files or calling APIs.

## Requirements

- **Java JDK 17 or newer**
    - Download: https://www.oracle.com/java/technologies/downloads/

- **Apache Maven**
    - Used to download dependencies and run tests.
    - Download: https://maven.apache.org/download.cgi

- **IntelliJ IDEA** *(recommended)*
    - Any Java IDE with Maven support can be used.
    - Download: https://www.jetbrains.com/idea/download/
  
- A desktop operating system capable of running Java Swing:
  - Windows
  - macOS
  - Linux
- Internet access is optional.
  - Required only for live Cito UFC data.
  - Local fallback data is available if the API cannot be reached.
- IntelliJ IDEA is recommended for running the graphical application, but any Java IDE with Maven support can be used.

### Maven Dependencies

Maven downloads the following project dependencies automatically:

- `org.json:json:20240303`
- `com.squareup.okhttp3:okhttp:4.12.0`
- `junit:junit:4.13.1`
- `org.junit.jupiter:junit-jupiter:5.8.1`

## Installation

### 1. Clone the Repository

```bash
git clone https://github.com/CSC207-Prj/15-0.git
cd 15-0
```

### 2. Install / Select a Java JDK

Make sure Java 16 or newer is installed.

Check with:

```bash
java -version
```

### 3. Open the Project as a Maven Project

In IntelliJ IDEA:

1. Select **Open**.
2. Choose the `15-0` project folder.
3. Allow IntelliJ to import the Maven project.
4. Set the Project SDK to Java 16 or newer.
5. Reload Maven if dependencies have not downloaded automatically.

### 4. Run the Tests

From the project root:

```bash
mvn test
```

### 5. Optional: Configure Live Cito UFC Data

See [Cito API Setup](#cito-api-setup).

## Cito API Setup

The application can run without a Cito key by using local fallback data. To use live UFC data, create a file called:

```text
cito.local.properties
```

in the **project root**, beside `pom.xml`.

Example:

```properties
cito.api.key=PASTE_YOUR_CITO_API_KEY_HERE
cito.api.baseUrl=https://api.citoapi.com/api/v1
```

Replace the placeholder with your own key.

The real `cito.local.properties` file must **not** be committed. The repository's `.gitignore` should contain:

```gitignore
cito.local.properties
.cito-cache/
```

The API client caches successful GET responses locally for 24 hours. If the live service is unavailable, the program can use cached or fallback data.

## Running the Program

The simplest method is through IntelliJ IDEA:

1. Open `src/main/java/app/Main.java`.
2. Run the `Main` class.
3. Make sure the working directory is the repository root, for example:

```text
C:\Users\YourName\...\15-0
```

or IntelliJ's:

```text
$ProjectFileDir$
```

The application opens as a Java Swing desktop window.

## Usage Guide

### Start a New Run

From the Welcome screen, select **New Run**.

Choose:

- Difficulty.
- Number of rounds.
- UFC era.
- Whether to hide opponent ratings.

Continue to Fighter Creation.

### Build a Fighter

1. Select **Spin Fighter**.
2. Review the displayed fighter and ratings.
3. Select an unfilled attribute.
4. Select **Assign Attribute**.
5. Repeat until all six attributes are filled.
6. Use **Reroll Fighter** when available if you want a different source fighter.
7. Continue when the fighter is complete.

### Confirm the Fighter

1. Review the six drafted attributes.
2. Enter a fighter name.
3. Spin for a weight class.
4. Review the calculated overall rating.
5. Confirm the fighter.

### Run the Gauntlet

The Simulation screen begins at ranked opponent #15.

You can:

- Simulate the next fight.
- Auto-simulate the remaining fights.
- Review fight history.
- Track the current record.
- See the next opponent.
- Save the fighter once all 15 fights are complete.

### Saved Fighters

From the home screen, open **Saved Fighters** to:

- View your roster.
- Load a fighter.
- Delete a fighter.
- View top fighters.
- Run an exhibition match between two saved fighters.

### Fighter Browser

From the home screen, open **Fighter Browser** to:

- Search by fighter name.
- Filter by division.
- Filter by UFC era.
- Select a fighter and inspect their profile and ratings.

## Testing

Run the full automated test suite with:

```bash
mvn test
```

The project includes tests for entities and use cases, including areas such as:

- Fighter creation and attribute assignment.
- Game settings.
- Fighter confirmation.
- Gauntlet progression.
- Fight simulation.
- Loss handling and completion after 15 fights.
- Saved-fighter operations.
- Fighter Browser filtering/searching.
- API mapping and data-access behavior.

Randomized fight behavior is made testable through the `RandomSource` abstraction, which allows deterministic test implementations to replace production randomness.

## Project Structure

```text
15-0/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── app/
│   │       ├── data_access/
│   │       ├── entity/
│   │       ├── interface_adapter/
│   │       ├── use_case/
│   │       └── view/
│   └── test/
│       └── java/
├── pom.xml
├── README.md
├── accessibility-report.md
└── cito.local.properties.example   # if included in the repository
```

Runtime-only files such as `saved_fighters.json`, `.cito-cache/`, and `cito.local.properties` should not be committed.

## Accessibility

The project includes several accessibility-conscious choices such as:

- Large, consistently styled buttons and headings.
- High-contrast text on a dark interface.
- Text labels alongside graphical stat bars.
- Textual WIN/LOSS and fight-history information rather than relying only on colour.
- Clear progress indicators such as attribute count, reroll count, record, and next opponent.
- Auto-simulation to reduce repetitive interaction.

The current application also has limitations, particularly around keyboard-only interaction, scalable text, and screen-reader testing. See [accessibility-report.md](accessibility-report.md) for the full Universal Design assessment and proposed improvements.

## Common Issues

### `mvn` is not recognized

Maven is either not installed or is not on the system `PATH`.

Install Maven, restart the terminal/IDE, and verify:

```bash
mvn -version
```

### Java version errors

Make sure the project SDK and Maven compiler use Java 16 or newer.

In IntelliJ, check:

```text
File → Project Structure → Project SDK
```

### Live UFC data does not appear

Check that:

1. `cito.local.properties` is in the project root.
2. The key is not still the placeholder.
3. The application's working directory is the project root.
4. Internet access is available.

If live data cannot be loaded, the program may continue using cached or fallback data.

### A fresh API request is not being made

The project uses a local `.cito-cache/`. Delete that directory while debugging if you need to force new API requests.

### Saved fighters do not appear

The roster is stored in:

```text
saved_fighters.json
```

in the application's working directory. Make sure the application is being launched from the expected project directory.

## Data, Privacy, and Local Files

The Cito API key is stored only in the developer's local `cito.local.properties` file and should never be committed to GitHub.

The program may create:

```text
cito.local.properties
.cito-cache/
saved_fighters.json
target/
```

These are local configuration, cache, runtime data, or build output and should not be included in a public submission unless specifically required.

Before sharing a manual ZIP of the project, remove any private API key file.

## Feedback

Feedback can be submitted through the GitHub repository's Issues section.

Useful feedback should include:

- A clear description of the problem or suggestion.
- Steps to reproduce a bug.
- Expected behavior.
- Actual behavior.
- Java version and operating system when relevant.
- Screenshots or console output when helpful.
- No API keys, passwords, or other private information.

For bugs, please check existing issues before opening a duplicate.

## Contributing

Contributions should preserve the project's Clean Architecture boundaries and existing coding conventions.

Recommended process:

1. Fork the GitHub repository.
2. Create a focused feature or bug-fix branch.
3. Make small, logically related commits.
4. Add or update tests for behavior changes.
5. Run:

```bash
mvn test
```

6. Push the branch to your fork.
7. Open a pull request against the project's `main` branch.
8. In the pull request, describe:
   - What changed.
   - Which user story or bug is affected.
   - How the change was tested.
   - Any known limitations.

Pull requests should be reviewed before merging. Avoid mixing unrelated refactoring and feature work in the same pull request.

## License

This repository is a CSC207 course project and currently does **not** include an open-source license granting public reuse, modification, or redistribution rights.

Unless the team adds an explicit `LICENSE` file, reuse of the source code requires permission from the project contributors. If the project is released publicly beyond the course, the team should choose and add an appropriate software license first.
