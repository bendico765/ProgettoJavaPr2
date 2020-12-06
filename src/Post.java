import java.util.HashSet;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;

public class Post implements Comparable<Post>{
  /*
    OVERVIEW: Post è un tipo di dato astratto modificabile che descrive un generico
    post di un social network.

    Elemento tipico: <id, author, text, timestamp, usersLike}>

    AF(c): f(id, author, [char_0, char_1, ... , char_maxCharText-1], timestamp, usersLike, idCounter) -->
            <id, author, text, timestamp, usersLike>
    IR(c): id >= 0 && author != null && ( text != null && text.length <= MAX_CHAR_TEXT ) && timestamp != null &&
           usersLike != null &&
           usersLike.contains(null) == false &&
           usersLike.contains(author) == false &&
           forall j,k. j,k in [0, usersLike.size()) && j!=k => usersLike.toArray()[j] != usersLike.toArray()[k]
  */
  private int id; // id del post
  private String author; // username dell'autore
  private char text[]; // testo del post
  private Timestamp timestamp; // data di pubblicazione del post
  private HashSet<String> usersLike; // set di utenti che hanno messo like al post
  private static int idCounter = 0; // contatore incrementale

  private final int MAX_CHAR_TEXT = 140; // limite massimo lunghezza testo
  // Messaggio di errore da stampare se il testo supera la dimensione massima consentita
  private final String TEXT_TOO_LONG_ERROR_MESSAGE = String.format("Text length must be less or equal than %d.", MAX_CHAR_TEXT);
  // Messaggio di errore se l'autore del post prova a mettere like
  private final String AUTHOR_CANT_LIKE_ERROR_MESSAGE = "The author of the post can't like himself.";

  /*
    EFFECTS: ritorna true se l'invariante di rappresentazione vale per this,
           falso altrimenti
  */
  public boolean checkRep(){
    if(id < 0 || author == null || text == null || timestamp == null || usersLike == null){
      return false;
    }
    if( text.length > MAX_CHAR_TEXT ){
      return false;
    }
    if( usersLike.contains(null) || usersLike.contains(author)){
      return false;
    }
    return true;
  }

  /*
    Metodo costrutture senza parametri

    MODIFIES: this
    EFFECTS: Inizializza un post vuoto ed incrementa in il contatore degli id di 1, formalmente:
             IR(this_post) && ( idCounter_post = idCounter_pre + 1 )
  */
  public Post(){
    this.id = this.idCounter;
    this.author = new String();
    this.text = new char[0];
    this.timestamp = new Timestamp(0);
    this.usersLike = new HashSet<String>();
    this.idCounter += 1;
  }

  /*
    Metodo costruttore che permette di inizializzare il post con parametri

    REQUIRES: author != null && ( text != null && text.size <= MAX_CHAR_TEXT)
              && timestamp != null && usersLike != null &&
              usersLike.contains(null) == false &&
              usersLike.contains(author) == false &&
              forall j,k. j,k in [0, usersLike.size()) && j!=k => usersLike.toArray()[j] != usersLike.toArray()[k]
    MODIFIES: this
    THROWS: NullPointerException
              se ( author == null || text == null || timestamp == null || usersLike == null || usersLike.contains(null))
            TextTooLongException se text.length >= MAX_CHAR_TEXT
            AuthorCantLikeHimselfException se usersLike.contains(author)
    EFFECTS: Inizializza gli attributi di this con i parametri passati come parametro e l'id con
             il valore del contatore statico (incrementato successivamente di 1); prima di
             salvarli in this viene fatta una deep copy degli oggetti passati.
  */
  public Post(String author, char text[], Timestamp timestamp, HashSet<String> usersLike) throws NullPointerException, TextTooLongException, AuthorCantLikeHimselfException{
    if( author == null || text == null || timestamp == null || usersLike == null || usersLike.contains(null)){
      throw new NullPointerException();
    }
    if( text.length >= MAX_CHAR_TEXT ){
      throw new TextTooLongException(TEXT_TOO_LONG_ERROR_MESSAGE);
    }
    if( usersLike.contains(author) ){
      throw new AuthorCantLikeHimselfException("The author of the post can't like himself.");
    }
    this.id = this.idCounter;
    this.author = author;
    this.text = Arrays.copyOf(text, text.length);
    this.timestamp = new Timestamp(timestamp.getTime());
    this.usersLike = new HashSet<String>(usersLike);
    this.idCounter += 1;
  }

  /*
    Metodo costruttore che permette di inizializzare il nuovo Post con
    le stesse informazioni di un Post passato come parametro.

    MODIFIES: this
    THROWS: NullPointerException
              se oldPost == null
    EFFECTS: Inizializza il post corrente con gli stessi campi di oldPost
  */
  public Post(Post oldPost) throws NullPointerException{
    if( oldPost == null ){
      throw new NullPointerException();
    }
    this.id = oldPost.getId();
    this.author = oldPost.getAuthor();
    this.text = oldPost.getText();
    this.timestamp = oldPost.getTimestamp();
    this.usersLike = oldPost.getUsersLike();
  }

  /*
    La funzione permette di modificare il testo corrente del post con un
    testo passato come parametro, il quale non deve comunque eccedere la
    dimensione massima.

    REQUIRES: newText != null && newText.length <= MAX_CHAR_TEXT
    MODIFIES: this
    THROWS: NullPointerException se newText == null
            TextTooLongException se newText.length >= MAX_CHAR_TEXT
    EFFECTS: text_post = newText
  */
  public void editText(char newText[]) throws NullPointerException, TextTooLongException{
    if( newText == null ){
      throw new NullPointerException();
    }
    if( newText.length >= MAX_CHAR_TEXT ){
      throw new TextTooLongException(TEXT_TOO_LONG_ERROR_MESSAGE);
    }
    else{
      this.text = Arrays.copyOf(newText, newText.length);
    }
  }

  /*
    EFFECTS: restituisce l'id del post.
  */
  public int getId(){
    return this.id;
  }

  /*
    EFFECTS: restituisce l'autore del post.
  */
  public String getAuthor(){
    return this.author;
  }

  /*
    EFFECTS: restituisce una deep copy del testo
  */
  public char[] getText(){
    return Arrays.copyOf(text, text.length);
  }

  /*
    EFFECTS: restituisce una deep copy del timestamp del post
  */
  public Timestamp getTimestamp(){
    return new Timestamp(this.timestamp.getTime());
  }

  /*
    EFFECTS: restituisce una deep copy del set di utenti che hanno messo like al post
  */
  public HashSet<String> getUsersLike(){
    return new HashSet<String>(this.usersLike);
  }

  /*
    Restituisce vero se il testo del post contiene l'argomento word,
    falso altrimenti. La ricerca della parola all'interno del testo
    non è case sensitive e fa uso di espressioni regolari.

    REQUIRES: word != null
    THROWS: NullPointerException se word == null
    EFFECTS: Ritorna true se text contiene word, false altrimenti
  */
  public boolean doesContainWord(String word) throws NullPointerException{
    if( word == null ){
      throw new NullPointerException();
    }
    String regex = "\\b" + word + "\\b";
    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE); // creazione del pattern della parola, con flag CASE_INSENSITIVE
    Matcher matcher = pattern.matcher(new String(this.text)); // la classe Matcher contiene informazioni sull'esito della ricerca della parola
    return matcher.find();
  }

  /*
    Aggiunge l'username passato come parametro al set di utenti che
    hanno messo like al post. Un utente non viene inserito nella lista se
    ha già messo like o se è l'autore del post stesso.
    Ritorna vero se l'inserimento è andato a buon fine, falso altrimenti.

    REQUIRES: username != null && username != author
    MODIFIES: this
    THROWS: NullPointerException se username == null
            AuthorCantLikeHimselfException se username == author
    EFFECTS:
      Se usersLike_pre.contains(username) == false allora
        usersLike_post = usersLike_pre U {username}
      altrimenti ritorna falso
  */
  public boolean addLike(String username) throws NullPointerException, AuthorCantLikeHimselfException{
    if( username == null ){
      throw new NullPointerException();
    }
    if( username == this.author ){
      throw new AuthorCantLikeHimselfException(AUTHOR_CANT_LIKE_ERROR_MESSAGE);
    }
    return usersLike.add(username);
  }

  /*
    Rimuove il like dell'utente il cui username è passato come parametro.
    Ritorna vero se la rimozione è andata a buon fine, falso se l'elemento
    non era presente nel set.

    REQUIRES: username != null
    MODIFIES: this
    THROWS: NullPointerException se username == null
    EFFECTS: Se usersLike.contains(username) allora
              usersLike_post = usersLike_pre \ {username} e ritorna true,
             altrimenti ritorna falso
  */
  public boolean removeLike(String username) throws NullPointerException {
    if( username == null ){
      throw new NullPointerException();
    }
    return usersLike.remove(username);
  }

  /*
    Metodo per confrontare this con un qualsiasi altro oggetto;
    ritorna true se obj estende Post ed obj e this hanno il medesimo id,
    falso altrimenti

    EFFECTS: Ritorna vero se obj istanceof Post && this.id == convertedObj.getId(),
    falso altrimenti.
  */
  public boolean equals(Object obj){
    if (obj instanceof Post){
      Post convertedObj = (Post) obj;
      return this.id == convertedObj.getId();
    }
    else{
      return false;
    }
  }

  /*
    Confronta l'oggetto Post passato come parametro con this,
    restituendo il risultato del compareTo tra le Timestamp
    dei due post

    REQUIRES: otherPost != null
    THROWS: NullPointerException se otherPost == null
    EFFECTS: Ritorna il risultato del compareTo tra la timestamp del post corrente
             e quella di otherPost.
  */
  public int compareTo(Post otherPost) throws NullPointerException{
    if( otherPost  == null){
      throw new NullPointerException();
    }
    return this.timestamp.compareTo(otherPost.getTimestamp());
  }

  /*
    EFFECTS: Il metodo restituisce una rappresentazione testuale di this
  */
  public String toString(){
    return String.format("{%d,%s,%s,%s,%s}", id, author,
    String.valueOf(text), timestamp, usersLike.toString());
  }
}
