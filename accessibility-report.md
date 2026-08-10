# Project Accessibility Report

## 15-0: The Build-A-Fighter Gauntlet

This report evaluates **15-0** using the seven Principles of Universal Design and identifies both current accessibility-supporting features and areas that could be improved. The assessment treats accessibility as an interaction between a user's needs and the way the software environment is designed, rather than assuming that limitations belong only to the user.

## 1. Principles of Universal Design

### Principle 1: Equitable Use

The application presents the same core game features to all users rather than separating users into a reduced "accessible" version. Important simulation information such as fighter names, records, attribute values, WIN/LOSS results, and fight history is presented as text, so users are not required to interpret colour alone. However, the current Swing interface has not been thoroughly tested with screen readers or other assistive technologies; a future version should add explicit accessible names/descriptions to controls and test the program with common assistive software.

### Principle 2: Flexibility in Use

The game provides several ways to control the experience: users can select difficulty, round count, UFC era, whether opponent ratings are hidden, and whether fights are simulated individually or automatically. The Fighter Browser also provides search and multiple filters, allowing users to locate information in different ways. A limitation is that some interactions, especially selecting fighter attributes, are strongly mouse-oriented; future work should ensure every action is fully operable by keyboard and add keyboard shortcuts for common actions.

### Principle 3: Simple and Intuitive Use

The program follows a consistent linear flow from Game Settings to Fighter Creation, Confirm Fighter, and Gauntlet Simulation. Screens use descriptive headings and clearly named actions such as **Spin Fighter**, **Assign Attribute**, **Confirm Fighter**, **Simulate Next Fight**, and **Save Fighter**, while progress labels show information such as attributes completed, rerolls remaining, current record, and next opponent. The consistent theme and repeated Back/Continue navigation reduce the need for users to learn a different interaction model on every screen.

### Principle 4: Perceptible Information

The interface uses high-contrast light text against dark backgrounds, large headings, and body text that is generally larger than Swing's default sizing. Gameplay stat bars are accompanied by numerical/text labels, and fight outcomes use explicit words such as **WIN**, **LOSS**, **KO/TKO**, **SUBMISSION**, and **DECISION**, so meaning is not communicated only through colour or graphics. The main limitation is that text size is currently fixed; future versions should support user-adjustable font scaling, a high-contrast/theme option, and verified screen-reader descriptions for controls and dynamic status messages.

### Principle 5: Tolerance for Error

The program includes validation at multiple layers. Entities and interactors reject invalid states, fighter creation limits rerolls and prevents incomplete progression, confirmation requires the fighter to be complete, and the API layer can fall back to cached or local data instead of crashing when the external service is unavailable. The Saved Fighters screen currently allows deletion with little protection from accidental activation, so a confirmation dialog or undo option would improve tolerance for error for destructive actions.

### Principle 6: Low Physical Effort

Most of the game is controlled through large buttons, selections, and short interactions rather than long text entry or repeated complex gestures. The **Auto Simulate** option is especially useful because a player does not need to manually activate the simulation fifteen separate times, and the Fighter Browser allows filtering instead of requiring users to scroll through every fighter. More keyboard support and customizable shortcuts would further reduce physical effort for users who have difficulty with repeated mouse movement.

### Principle 7: Size and Space for Approach and Use

The interface uses relatively large button targets, generous spacing, visible section headings, and scroll panes where lists may become long. This makes controls easier to identify and select than very small or densely packed controls. The application is designed primarily for a desktop window with a fairly large minimum size, so it may be difficult to use on small displays or at high magnification; a future version should use more responsive layouts and allow interface scaling without clipping content.

## 2. Intended Market / Audience

If the program were sold or licensed, its main audience would be UFC and mixed-martial-arts fans who enjoy strategy and sports-simulation games. It would especially appeal to users who like comparing fighters, experimenting with different builds, and seeing how combinations of real fighter strengths perform against ranked opponents. The game is also designed to be approachable for more casual UFC viewers because the core interaction is based on familiar concepts such as fighter ratings, records, rankings, and fight results rather than requiring detailed knowledge of MMA statistics. A secondary audience could include sports-game players who enjoy replayability, randomized drafting, roster management, and "what-if" matchups.

## 3. Demographics and Potential Barriers

The program may currently be less likely to be used by some groups because of design barriers rather than because those users are inherently unable to play it. For example, users with low vision may be affected by fixed font sizes, users with motor disabilities may encounter difficulty with mouse-oriented attribute selection, and users relying on screen readers may face problems because the Swing interface has not yet been comprehensively tested with assistive technology. The program is also English-only and focused on UFC/combat sports, which may reduce interest or usability for non-English-speaking users or people who are uncomfortable with simulated fighting. From the social model of disability, these are reasons to improve the software environment—for example through full keyboard navigation, screen-reader labels, scalable text, alternative colour themes, localization, and accessibility testing—rather than treating the affected users themselves as the problem.
