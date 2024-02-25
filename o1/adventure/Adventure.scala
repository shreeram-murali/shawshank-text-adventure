package o1.adventure

/** The class `Adventure` represents text adventure games. An adventure consists of a player and
  * a number of areas that make up the game world. It provides methods for playing the game one
  * turn at a time and for checking the state of the game.
  *
  * N.B. This version of the class has a lot of “hard-coded” information that pertains to a very
  * specific adventure game that involves a small trip through a twisted forest. All newly created
  * instances of class `Adventure` are identical to each other. To create other kinds of adventure
  * games, you will need to modify or replace the source code of this class. */
class Adventure:

  /** the name of the game */
  val title = "A Prison Escape Adventure based on The Shawshank Redemption"

  private val lobby = Area("Lobby", "You, Andy Dufresne, have been falsely arrested for murdering your wife.\nYou are about to enter the Shawshank State prison.")
  private val canteen = Area("Canteen", "You're at the canteen -- the place where all inmates gather to get some not-so-delicious food!\nMaybe you can make a new friend here!\nYou notice Red, maybe you can talk to him.")
  private val library = Area("Library", "You are at the prison library. You see a few books.")
  private val wardenRoom = Area("Warden's Room", "You are sometimes summoned here to file some paperwork for the warden's embezzlement.\nHowever, you've got other ideas!\nYou can talk to the warden to see what's up.")
  private val playground = Area("Playground", "You are at the open playground! The ground is muddy and strewn with small rocks.")
  private val mexico = Area("Zihuatanejo", "You're finally free at last!\nYou reach Zihuatanejo, a beautiful town on the Pacific coast of Mexico!")
  private val prison = Area("Prison Cell", "You are at your prison cell.\nThere's not much to do here ... or is there?")
  private val destination = mexico

  lobby.setNeighbors(Vector("south" -> canteen))
  canteen.setNeighbors(Vector("north" -> prison, "east" -> playground, "south" -> library))
  library.setNeighbors(Vector("north" -> canteen, "east" -> wardenRoom))
  wardenRoom.setNeighbors(Vector("west" -> library))
  playground.setNeighbors(Vector("west"-> canteen))
  prison.setNeighbors(Vector("south" -> canteen))

  private val hammer = Hammer
  private val poster = Poster

  canteen.addItem(hammer)
  canteen.addItem(poster)

  /** The character that the player controls in the game. */
  val player = Player(lobby)

  /** The number of turns that have passed since the start of the game. */
  var turnCount = 0
  /** The maximum number of turns that this adventure game allows before time runs out. */
  //val wardenInspection = 5
  val timeLimit = 30

  /** Determines if the adventure is complete, that is, if the player has won. */
  def isComplete = this.player.location == this.destination && this.player.canEscape

  /** Determines whether the player has won, lost, or quit, thereby ending the game. */
  def isOver = this.isComplete || this.player.hasQuit || this.turnCount == this.timeLimit

  /** Returns a message that is to be displayed to the player at the beginning of the game. */
  def welcomeMessage = "You have been wrongly convicted for murdering your wife.\nYou must dig your way out of the Shawshank State Prison now."

  /** Returns a message that is to be displayed to the player at the end of the game. The message
    * will be different depending on whether or not the player has completed their quest. */
  def goodbyeMessage =
    if this.isComplete then
      "Zihuatanejo, at last! Enjoy your freedom in this beautiful Mexican town that sits on the Pacific coast!"
    else if this.turnCount == this.timeLimit then
      "Oh no! Time's up. Looks like you will have to serve life in prison.\nGame over!"
    else  // game over due to player quitting
      "Quitter!"

  /** Plays a turn by executing the given in-game command, such as “go west”. Returns a textual
    * report of what happened, or an error message if the command was unknown. In the latter
    * case, no turns elapse. */
  def playTurn(command: String) =
    val action = Action(command)
    val outcomeReport = action.execute(this.player)
    if outcomeReport.isDefined then
      this.turnCount += 1
    if this.player.canEscape then
      prison.setNeighbors(Vector("east" -> mexico))
    outcomeReport.getOrElse(s"""Unknown command: "$command".""")

end Adventure

