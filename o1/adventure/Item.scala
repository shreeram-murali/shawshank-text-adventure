package o1.adventure

/** The class `Item` represents items in a text adventure game. Each item has a name
  * and a longer description. (In later versions of the adventure game, items may
  * have other features as well.)
  *
  * N.B. It is assumed, but not enforced by this class, that items have unique names.
  * That is, no two items in a game world have the same name.
  *
  * @param name         the item’s name
  * @param description  the item’s description */

trait Item(val name: String, val description: String):
  def use(player: Player): String
  override def toString = this.name
end Item

object Hammer extends Item("hammer", "Just a rock hammer that you could use to dig tunnels into walls!"):
  def use(player: Player): String =
    if player.isAtPrisonCell then
      player.hammerFlag = true
      player.addRocks()
      "Rocks have been added to your inventory. Drop them at the playground."
    else
      "The hammer isn't of much use here."
end Hammer

object Poster extends Item("poster", "A poster of Rita Hayworth. Maybe you could use it to cover something?"):
    def use(player: Player): String =
      if player.isAtPrisonCell then
        player.posterFlag = true
        player.removePoster()
        "You apply the poster onto the wall of your prison cell."
      else
        "The poster isn't of much use here."
end Poster

object Rock extends Item("rocks", "A bunch of rocks.\nDon't hold on to them for too long, discard them in the playground!"):
  def use(player: Player): String = "These rocks aren't very useful."
end Rock

object Warden:
  val speak = "I am Warden Norton. Can you help me with some money laundering?"
end Warden

object Red:
  val speak = "Hey, I'm Red. I can give you a hammer and a poster. Maybe some birds are not meant to be caged!"
end Red