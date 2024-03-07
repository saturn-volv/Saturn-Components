# Saturn Components
A small fabric mod which will be used to generate extra features for Item Components I think may be of interest to datapack creators or for map makers.
## Max Stack Size Component
`minecraft:max_count`<br>
This component sets what the maximum count for an item can be. It accepts a simple non-negative integer value.


## Food Component
`minecraft:food_properties`<br>
This component adds functionality to turn *any* item into a food item with nothing more than an item component.
### Format:
The format can be seen in the following json code block:
```jsonc
{
  "food_properties": {
    // A non-negative integer. Decides how much hunger to restore when consumed.
    "hunger": 0,
    // A positive float. Decides how much saturation to restore when consumed.
    "saturation_modifier": 1.0,
    // A positive integer. Decides how long it takes to consume the item.
    "time_to_eat": 32,
    // Decides the behavior of the food.
    "food_behaviour": { // Soon to be changed to "food_behavior"
      // Whether wolves interact with the item.
      "is_meat": false,
      // Whether the item can be eaten regardless of a player's hunger value.
      "always_edible": false,
      // Whether the item is eaten in half the time.
      "is_snack": false,
      // Whether the item shows particles while being consumed.
      "is_drink": false,
      // Whether the item clears all the player's status effects after being consumed.
      "clear_effects": false
    },
    // The resulting item a food object should have once it's stack size reaches 0 after being consumed.
    "result_item": {
      "id": "minecraft:air",
      "count": 1
    },
    // A list of status effect objects that are applied to the player once they have consumed the item.
    "status_effects": [
      {
        "id": "minecraft:swiftness",
        "duration": 160,
        "amplifier": 0,
        "ambient": false,
        "show_particles": true,
        "chance": 1.0
      }
    ]
  }
}
```
