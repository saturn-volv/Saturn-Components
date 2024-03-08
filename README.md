# Saturn's Components
A fabric mod which adds some extra functionality to item components to allow for users to have more flexibility with datapack items.

The end goal for the mod is to allow for any item within the vanilla game to be recreated within any other item using only item components.

## Max Count
`minecraft:max_count`<br>
Sets the maximum stack size an item can have, accepting a non-negative integer value.

## Max Damage
`minecraft:max_damage`<br>
Sets the total durability of an item, which affects non-tool items too! Accepts a non-negative integer value.

## Rarity
`minecraft:rarity`<br>
Sets the rarity of an item, determining the base color of it's display name. Accepts a valid string entry from the following list: `["common", "uncommon", "rare", "epic"]`

## Fireproof
`minecraft:fireproof`<br>
Sets whether the item will be destroyed when coming into contact with a fire source. Accepts a boolean value.

## Food Properties
`minecraft:food_properties`<br>
This component completely removes any hard coding to how food items are introduced, meaning any item can now be edible with a quick command! This component is a large one and requires at minimum a hunger value to be specified. An example component can be seen as followed:
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
    "food_behavior": {
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
    // The resulting item a food object should give the player after being consumed.
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
