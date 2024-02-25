package o1.adventure

import scala.collection.mutable.Map

/** A `Player` object represents a player character controlled by the real-life user
  * of the program.
  *
  * A player object’s state is mutable: the player’s location and possessions can change,
  * for instance.
  *
  * @param startingArea  the player’s initial location */
class Player(startingArea: Area):

  private var currentLocation = startingArea        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false              // one-way flag
  private val items = Map[String, Item]()

  // Adding some flag variables that we use to determine a few conditions
  var hammerFlag: Boolean = false // set to true once hammer has been used thrice in prison cell
  var posterFlag: Boolean = false // true once posterFlag has been used in prison cell
  var paperworkFlag: Boolean = false // true once the paperwork has been filed for the warden

  // some help commands
  val allCommands: Map[String, String] =
    Map("go" -> "go in a specific compass direction",
      "rest" -> "does nothing apart from taking rest",
      "quit" -> "end the game",
      "inventory" -> "list out the items in the inventory",
      "get" -> "pick up an item",
      "drop" -> "drop an item",
      "examine" -> "examine an item",
      "use" -> "use an item",
      "paperwork" -> "file some paperwork",
      "help" -> "get some help",
      "talk" -> "talk to someone, if they're in the room")

  def canEscape: Boolean = this.hammerFlag && this.posterFlag && this.paperworkFlag
  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven

  /** Returns the player’s current location. */
  def location = this.currentLocation

  def isAtPrisonCell: Boolean = this.location.name == "Prison Cell"


  /** Attempts to move the player in the given direction. This is successful if there
    * is an exit from the player’s current location towards the direction name. Returns
    * a description of the result: "You go DIRECTION." or "You can't go DIRECTION." */
  def go(direction: String) =
    val destination = this.location.neighbor(direction)
    this.currentLocation = destination.getOrElse(this.currentLocation)
    if destination.isDefined then "You go " + direction + "." else "You can't go " + direction + "."


  /** Causes the player to rest for a short while (this has no substantial effect in game terms).
    * Returns a description of what happened. */
  def rest() =
    "You rest for a while. Better get a move on, though."


  /** Signals that the player wants to quit the game. Returns a description of what happened within
    * the game as a result (which is the empty string, in this case). */
  def quit() =
    this.quitCommandGiven = true
    "You've given up. You're stuck in prison now. :("

  def inventory: String =
    if this.items.isEmpty then
      "You are empty-handed."
    else
      "You are carrying:\n" + this.items.keys.mkString("\n")

  def has(itemName: String): Boolean = this.items.contains(itemName)

  def examine(itemName: String): String =
    val item = this.items.get(itemName)
    if item.isDefined then
      s"You look closely at the ${itemName}.\n" + item.get.description
    else
      "If you want to examine something, you need to pick it up first."

  def use(itemName: String): String =
    this.items.get(itemName) match
      case Some(x) => x.use(this)
      case None => s"You don't have ${itemName} to use!"

  def talk(): String =
    if this.location.name == "Warden's Room" then
      Warden.speak
    else if this.location.name == "Canteen" then
      Red.speak
    else
      "There is no one here to talk to :("

  def get(itemName: String): String =
    val item = this.location.removeItem(itemName)
    if item.isDefined then
      this.items += (itemName -> item.get)
      s"You pick up the ${itemName}."
    else
      s"There is no ${itemName} here to pick up."

  def addRocks(): String =
    this.items += ("rocks" -> Rock)
    "Rocks have been added to your inventory, having used the hammer"

  def removePoster(): String =
    this.items.remove(Poster.name)
    "The poster has been removed from your inventory."

  def paperwork: String =
    if this.location.name == "Warden's Room" then
      this.paperworkFlag = true
      "You've filled in the paperwork!\nBut you've got a trick up your sleeve! ;)"
    else
      "There's no paperwork to do here."

  def drop(itemName: String): String =
    val item = this.items.remove(itemName)
    if item.isDefined then
      this.location.addItem(item.get)
      s"You drop the ${itemName}"
    else
      "You don't have that!"


  def help(command: String): String =
    val generalHelp = "You must get the hammer and rocks from the canteen, use them in the prison cell, and then do paperwork at Warden's room." +
      "\nOnce you do them, you can go back to the prison cell and go east to escape." +
      "\n ----------" +
      "\n The commands available are: go, rest, quit, inventory, get, drop, examine, use, paperwork, and help."

    val specificHelp: String =
      if command == "" then
        this.allCommands.mkString("\n")
      else
        this.allCommands.get(command) match
         case Some(description) => description
         case None => "No such command available."

    if command == "" then
      generalHelp + "\n" + specificHelp
    else
      specificHelp

  /** Returns a brief description of the player’s state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name

end Player

