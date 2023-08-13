# Final Year University Project: Evolutionary Algorithms Applied To Neural Network AI

This was an ambitious project to develop a functional desktop app in Java from concept to deployment.

## Concept

The idea was to code a simulation of a population of artifical life "creatures" (termed bots), each equipped with a neural network "brain" whose structure was determined by a serial encoding ("genome"). The bots would navigate a simple environment with movement costing energy, eat to gain energy, and die if they ran out of energy. Survivors or those deemed fittest by configurable categories would reproduce, mixing and mutating their genomes to spawn new bots with new (and hopefully better) brain structures.

The simulation would be visualised in real time, and simulation factors could be set via a GUI.

## Outcome

The project was completed on time with all key requirements met. In particular, the real time simulation produces noticable advances in bot behaviour within a reasonable timeframe (5 or so minutes). Not only that, but from entirely randomly generated structures the bots evolved to display very organic looking behaviour including turning at environment boundaries, seeking food, slowing thier movement to graze at high food density areas, and even producing a feedback loop between themselves and the environment where heavy grazing at the edges of areas with a high food density would generate an extreme gradient in nearby food density which the bots' senses could exploit to both navigate and allow areas of food to regrow.  

## Screenshots

![Program screenshot](https://github.com/khandrew221/Final-Year-Full-Stack-Project/blob/Post-Prototype/screenshot1.png)

## Architecture

![Architecture](https://github.com/khandrew221/Final-Year-Full-Stack-Project/blob/Post-Prototype/architecture.png)
