# NFL Fourth Down Calculator

## About the Project

I created this project to combine my interests in football, programming, and statistics. I wanted to see how historical NFL data could be used to help teams decide whether to go for it, kick a field goal, or punt on fourth down.

The program is written in Java and uses real play-by-play data from the 2025 NFL season. It combines historical conversion rates with football decision-making rules to recommend a play based on the situation.

## Features

- Analyzes 931 fourth-down run and pass plays from the 2025 NFL season.
- Calculates conversion rates based on yards needed and field position.
- Takes the score difference and time remaining into consideration.
- Uses distance-only statistics when there aren't enough plays matching a specific situation.
- Displays the recommendation, conversion rate, sample size, and confidence level.
- Uses Java Swing for user input and displaying results.
- Handles invalid inputs without crashing the program.

## How It Works

The program asks the user for four inputs:

- Yards needed for a first down
- Yards to the end zone
- Score difference
- Time remaining in seconds

It then searches through the NFL dataset for plays with the same yards needed and a similar field position (within 10 yards).

If there are fewer than 20 matching plays, the program calculates the conversion rate using only the yards needed. If there still isn't enough data, the recommendation relies on predefined football rules.

The program also prioritizes certain game situations, such as being down by three points late in the game while within field goal range.

## Example

**Input:**

- Yards needed: 2
- Yards to end zone: 45
- Score difference: 0
- Time remaining: 600 seconds

**Output:**

- Recommendation: GO FOR IT
- Conversion rate: 61.70%
- Matching plays: 47
- Decision source: NFL statistics
- Confidence level: Medium
- Statistical source: Distance and Field Position

## Technologies Used

- Java
- Java Swing
- Apache Commons CSV
- IntelliJ IDEA
- nflverse NFL play-by-play data

## How to Run

1. Clone or download the repository.
2. Open the project in IntelliJ IDEA.
3. Add the required Apache Commons CSV, IO, and Codec libraries.
4. Download the 2025 NFL play-by-play CSV dataset from
   [nflverse](https://github.com/nflverse/nflverse-data/releases/tag/pbp).
   Extract the file and place `play_by_play_2025.csv` in the project root directory.
5. Run `FourthDownCalculator.java`.

## What I Learned

While working on this project, I gained experience with reading and filtering large CSV files, working with real-world data, and designing an algorithm that combines statistics with rule-based decisions.

I also learned how important input validation, exception handling, and testing are when developing an application. Testing different football situations helped me identify problems with my original decision logic and improve the algorithm.

## Limitations

The calculator currently uses data from only one NFL season and does not account for factors such as team strength, weather, or kicker accuracy.

The recommendations are based on historical conversion rates and predefined rules, so they are not guaranteed to be the best possible decisions. The confidence level is based on the number of matching plays rather than a formal statistical confidence measure.

## Future Improvements

- Add data from multiple NFL seasons.
- Include team-specific statistics.
- Calculate field goal success probabilities.
- Develop a win probability model.
- Improve the user interface.
- Explore using machine learning to improve recommendations.

## Data Source

The NFL play-by-play data used in this project comes from [nflverse](https://github.com/nflverse/nflverse-data).

## Application Screenshot

![Fourth Down Calculator Result](screenshots/resdid ults.png)