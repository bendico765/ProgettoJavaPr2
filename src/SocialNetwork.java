import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.Collection;

public class SocialNetwork{
  /*
    OVERVIEW: SocialNetwork è un tipo di dato astratto mutabile che rappresenta
    un generico social network dotato di post testuali, utenti e possibilità
    di interazione tra utenti.

	Typical Element: <userFollows, userPosts, userFollowers>
					 Dove userFollows, userPosts, userFollows sono funzioni tali che
          				userFollows(x) = { y | x segue y}
          				userPosts(x) = { y | y ha come autore x }
          				userFollowers(x) =  { y | y segue x }
    AF(c): <userFollowsMap, userPostMap, userFollowersMap> ==>
          <userFollows, userPosts, userFollowers>


    Notazione: dom(f) = { x | exist y = f(x)}
               |f(x)| <- cardinalità dell'insieme y = f(x)

    IR(c): userFollowsMap != null && userPostMap != null && userFollowersMap != null &&
           userFollowsMap.containsKey(null) == false &&
           userFollowsMap.containsValue(null) == false &&
           forall i. userFollowsMap.values().contains(i) => i.contains(null) == false &&
           forall i. userFollowsMap.containsKey(i) => (
            forall j,k. j,k in [0, userFollowsMap.get(i).size()) && j!=k => userFollowsMap.get(i).toArray()[j] != userFollowsMap.get(i).toArray()[k] ) &&
           userPostMap.containsKey(null) == false &&
           userPostMap.containsValue(null) == false &&
           forall i. userPostMap.values().contains(i) => i.contains(null) == false &&
           forall i. userPostMap.containsKey(i) => (
            forall j,k. j,k in [0, userPostMap.get(i).size()) && j!=k => userPostMap.get(i).toArray()[j] != userPostMap.get(i).toArray()[k] ) &&
           userFollowers.containsKey(null) == false &&
           userFollowers.containsValue(null) == false &&
           forall i. userFollowers.values().contains(i) => i.contains(null) == false &&
           forall i. userFollowers.containsKey(i) => (
            forall j,k. j,k in [0, userFollowers.get(i).size()) && j!=k => userFollowers.get(i).toArray()[j] != userFollowers.get(i).toArray()[k] ) &&
           forall i. userFollowsMap.containsKey(i) <=> userPostMap.containsKey(i) <=> userFollowersMap.containsKey(i) &&
           forall i. userPostMap.containsKey(i) => ( forall j. userPostMap.get(i).contains(j) => j.getAuthor().equals(i) ) &&
           forall postSet. userPostMap.values().contains(postSet) => (forall post. postSet.contains(post) => IR(post)) &&
           forall i. userFollowsMap.containsKey(i) => ( forall j. userFollowsMap.get(i).contains(j) => exist k in userPostMap.get(j) t.c. k.getUsersLike().contains(i) ) &&
           forall i. userFollowersMap.containsKey(i) => ( forall j. userFollowersMap.get(i).contains(j)  => exist k in userPostMap.get(i) t.c. k.getUsersLike().contains(j) )
  */
  private Map<String, Set<String>> userFollowsMap; // mappa f(utente) -> utenti che segue
  private Map<String, Set<Post>> userPostMap; // mappa g(utente) -> post utente
  private Map<String, Set<String>> userFollowersMap; // mappa h(utente) -> utenti che lo seguono

  // Messaggio di errore da stampare se si prova ad operare su un utente che non esiste nel social
  private final String USER_NOT_EXIST_EXCEPTION = "The user identified by the username doesn't exist in the system";
  // Messaggio di errore da stampare se si prova ad inserire nel social un utente che già esiste
  private final String USER_ALREADY_EXIST_EXCEPTION = "The user already exists";

  /*
    EFFECTS: ritorna true se l'invariante di rappresentazione vale per this,
             falso altrimenti
  */
  public boolean checkRep(){
    // controllo che le varie mappe non siano null
    if( userFollowsMap == null || userPostMap == null || userFollowersMap == null ){
      return false;
    }
    // per ogni mappa, controllo che non sia contenuta la chiave o il valore null,
    // e che un qualsiasi valore all'interno della mappa non contenga al proprio
    // interno null
    if(userFollowsMap.containsKey(null) || userFollowsMap.containsValue(null)){
      return false;
    }
    for(Collection coll: userFollowsMap.values()){
      if( coll.contains(null) ){
        return false;
      }
    }
    if(userPostMap.containsKey(null) || userPostMap.containsValue(null)){
      return false;
    }
    for(String username: userPostMap.keySet()){
      for(Post post: userPostMap.get(username)){
        if( post == null ){
          return false;
        }
      }
    }
    if(userFollowersMap.containsKey(null) || userFollowersMap.containsValue(null)){
      return false;
    }
    for(Collection coll: userFollowersMap.values()){
      if( coll.contains(null) ){
        return false;
      }
    }
    // controllo che il dominio delle tre mappe coincida
    Set<String> keySetUserPostMap = userPostMap.keySet();
    Set<String> keySetUserFollowsMap = userFollowsMap.keySet();
    Set<String> keySetUserFollowersMap = userFollowersMap.keySet();

    if( ! (keySetUserPostMap.equals(keySetUserFollowsMap) && keySetUserFollowsMap.equals(keySetUserFollowersMap) ) ){
      return false;
    }

    // Per ogni post nel social controllo che rispetti il repInv di Post
    // e che in userPostMap sia associato alla chiave del suo autore
    for(String username: userPostMap.keySet()){
      for(Post post: userPostMap.get(username)){
        if( post.checkRep() == false ){
          return false;
        }
        if( post.getAuthor().equals(username) == false ){
          return false;
        }
      }
    }
    /*
      Controllo che, per ogni utente-chiave nella userFollowsMap, il set
      di utenti associati alla chiave contenga esclusivamente utenti che hanno
      pubblicato almeno un post cui l'utente-chiave abbia messo like
    */
    for(String username: userFollowsMap.keySet()){
      for(String followedUser: userFollowsMap.get(username)){
        boolean existFlag = false;
        for(Post post: userPostMap.get(followedUser)){
          if( post.getUsersLike().contains(username) ){
            existFlag = true;
          }
        }
        if( existFlag == false ){
          return false;
        }
      }
    }
    /*
      Controllo che, per ogni utente-chiave nella userFollowersMap, il set
      di utenti associati alla chiave contenga esclusivamente utenti che
      hanno messo like ad almeno un post dell'utente-chiave
    */
    for(String username: userFollowersMap.keySet()){
      for(String follower: userFollowersMap.get(username)){
        boolean existFlag = false;
        for(Post post: userPostMap.get(username)){
          if( post.getUsersLike().contains(follower) ){
            existFlag = true;
          }
        }
        if( existFlag == false ){
          return false;
        }
      }
    }
    return true;
  }

  /*
    Metodo costruttore che inizializza un social vuoto

    MODIFIES: this
    EFFECTS: inizializza le strutture dati di this
  */
  public SocialNetwork(){
    this.userFollowsMap = new HashMap<>();
    this.userPostMap = new HashMap<>();
    this.userFollowersMap = new HashMap<>();
  }

  /*
    Restituisce la rete sociale derivata dalla lista di post passati come parametro

    REQUIRES: ps != null && ps.contains(null) == false
    THROWS: NullPointerException se ps == null || ps.contains(null)
    EFFECTS: Ritorna una mappa newUserFollowsMap tale che:
            newUserFollowsMap.containsKey(null) == false &&
            newUserFollowsMap.containsValue(null) )) false &&
            forall i. newUserFollowsMap.values().contains(i) => i.contains(null) == false &&
            forall i. newUserFollowsMap.containsKey(i) => forall j. newUserFollowsMap.get(i).contains(j) exist k in ps t.c. (k.getAuthor().equals(j) && k.getUsersLike.contains(i))
  */
  public static Map<String, Set<String>> guessFollowers(List<Post> ps) throws NullPointerException{
    if( ps == null || ps.contains(null)){
      throw new NullPointerException();
    }
    Map<String, Set<String>> newUserFollowsMap = new HashMap<String, Set<String>>();
    for(Post post: ps){ // itero i post della lista
      HashSet<String> usersLike = post.getUsersLike(); // lista degli utenti che hanno messo "Mi piace" al post
      for(String username: usersLike){
        if( !newUserFollowsMap.containsKey(username) ){ // se l'utente che ha messo like non è presente come chiave nella mappa, lo aggiungo
          newUserFollowsMap.put(username, new HashSet<String>());
        }
        newUserFollowsMap.get(username).add(post.getAuthor()); // ogni utente che ha messo like inizia a seguire l'autore
      }
      if( !newUserFollowsMap.containsKey(post.getAuthor()) ){ // se non è presente, aggiungo anche l'autore del post
        newUserFollowsMap.put(post.getAuthor(), new HashSet<String>());
      }
    }
    return newUserFollowsMap;
  }

  /*
    Dato un social, restituisce la lista di influencers, ossia di utenti che hanno più
    followers che persone che seguono.

    REQUIRES: followers != null &&
              followers.containsKey(null) == false &&
              followers.containsValue(null) == false &&
              forall key. followers.containsKey(key) => followers.get(key).contains(null) == false
    THROWS: NullPointerException se followers == null || followers.containsKey(null) ||
            followers.containsValue(null) || exist i. followers.containsKey(i) t.c. followers.get(i).contains(null)
    EFFECTS: Restituisce una lista di utenti menzionati influencersList tale che
             forall i. influencersList.contains(i) =>
             #{ k | k.equals(i) && forall j. followers.values().contains(j) => followers.get(j).contains(k) } >= followers.get(i).size()
  */
  public static List<String> influencers(Map<String, Set<String>> followers) throws NullPointerException{
    if( followers == null || followers.containsKey(null) || followers.containsValue(null) ){
      throw new NullPointerException();
    }
    for(String key: followers.keySet()){
      if( followers.get(key).contains(null) ){
        throw new NullPointerException();
      }
    }
    List<String> influencersList = new LinkedList<>(); // lista in cui salverò gli influencers
    LinkedList<String> usersWhoFollowSomebody = new LinkedList<>();
    for(Set<String> follows: followers.values()){ // inserisco tutti i follows in una lista
      usersWhoFollowSomebody.addAll(follows);
    }
    for(String usernameFollower: followers.keySet()){ // itero le chiavi della mappa di utenti
      // conto quante occorrenze dell'utente ci sono nella lista di follows
      int followersOfUser = Collections.frequency(usersWhoFollowSomebody, usernameFollower);
      if( followersOfUser > followers.get(usernameFollower).size() ){
        influencersList.add(usernameFollower);
      }
    }
    return influencersList;
  }

  /*
    Restituisce la lista di utenti menzionati, ossia gli utenti
    che seguono qualcuno oppure hanno scritto almeno un post, contenuti
    nel social

    EFFECTS: Ritorna un set mentionedUsersSet di utenti menzionati, tale che
    mentionedUsersSet != null &&
    mentionedUsersSet.contains(null) == false &&
    forall i. mentionedUsersSet.contains(i) =>
    (i in dom(userPosts) && |userPosts(i)| > 0 )  || ( i in dom(userFollowsMap) && |userFollows(i)| > 0 )
  */
  public Set<String> getMentionedUsers(){
    Set<String> mentionedUsersSet = new HashSet<String>();
    for(String username: userPostMap.keySet()){ // aggiungo tutti gli utenti che hanno pubblicato almeno un post
      if( userPostMap.get(username).isEmpty() == false ){ // controllo che l'utente abbia pubblicato almeno un post
        mentionedUsersSet.add(username);
      }
    }
    for(String username: userFollowsMap.keySet()){ // aggiungo tutti gli utenti che seguono qualcuno
      if( userFollowsMap.get(username).isEmpty() == false ){
        mentionedUsersSet.add(username);
      }
    }
    return mentionedUsersSet;
  }

  /*
    Restituisce la lista di utenti menzionati, ossia di utenti che
    seguono qualcuno oppure hanno scritto almeno un post, a partire da una
    lista di post.

    REQUIRES: postList != null && postList.contains(null) == false
    THROWS: NullPointerException se postList == null || postList.contains(null) == true
    EFFECTS: Ritorna un set mentionedUsersSet di utenti menzionati, tale che
    mentionedUsersSet != null &&
    mentionedUsersSet.contains(null) == false &&
    forall i. mentionedUsersSet.contains(i) ==>
    exist j. postList.contains(j) => (j.getAuthor().equals(i) || j.getUsersLike.contains(i))
  */
  public static Set<String> getMentionedUsers(List<Post> postList) throws NullPointerException{
    if( postList == null || postList.contains(null) ){
      throw new NullPointerException();
    }
    HashSet<String> tmp = new HashSet<String>();
    for(Post post: postList){
      tmp.add(post.getAuthor()); // aggiungo l'autore del post
      tmp.addAll(post.getUsersLike()); // aggiungo tutti gli utenti che hanno messo like
    }
    return tmp;
  }

  /*
    Restituisce la lista di post scritti dall'utente il cui username
    è passato come parametro

    REQUIRES: username in dom(userPosts)
    THROWS: UserNotExistException se userPostMap.containsKey(username)==false
    EFFECTS: Ritorna una lista di post listaPost = [post_1, ..., post_n] tale che
             listaPost != null &&
             listaPost.contains(null) == false &&
             forall i. listaPost.contains(i) => post in userPosts(username)
  */
  public List<Post>	writtenBy(String username) throws UserNotExistException{
    if( userPostMap.containsKey(username) ){
      return new LinkedList<Post>(userPostMap.get(username));
    }
    throw new UserNotExistException(USER_NOT_EXIST_EXCEPTION);
  }

  /*
    Filtra la lista di post passata come parametro, restituendo una lista di
    post scritti dall'utente identificato da username.

    REQUIRES: postList != null && postList.contains(null) == false
    THROWS: NullPointerException se postList == null || postList.contains(null)
    EFFECTS: Ritorna una lista di post userPostList tale che
             userPostList != null &
             userPostList.contains(null) == false &&
             forall i. userPostList.contains(i) =>
             ( postList.contains(post) && post.getAuthor().equals(username))
  */
  public static List<Post>	writtenBy(List<Post> postList, String	username) throws NullPointerException{
      if( postList == null || postList.contains(null)){
        throw new NullPointerException();
      }
      List<Post> userPostList = new LinkedList<Post>();
      for(Post post: postList){
        if( post.getAuthor().equals(username) ){
          userPostList.add(post);
        }
      }
      return userPostList;
  }

  /*
    Restituisce	la lista dei post presenti nella rete
    sociale	che includono	almeno una delle parole presenti nella lista delle
    parole argomento del metodo.
    È ricercata la parola esatta (ossia non viene considerata la parola sotto
    forma di sottostringa di un'altra parola) e la ricerca non è case sensitive.

    REQUIRES: words != null && words.contains(null) == false
    THROWS: NullPointerException se words == null || words.contains(null)
    EFFECTS: Ritorna una lista di post postList tale che
             postList != null &&
             postList.contain(null) == false &&
             forall i. postList.contains(i) => exist w. words.contains(w) t.c. i.doesContainWord(w)
  */
  public List<Post> containing(List<String>	words) throws NullPointerException{
    if( words == null || words.contains(null)){
      throw new NullPointerException();
    }
    List<Post> postList = new LinkedList<Post>();
    for(String username: userPostMap.keySet()){ // itero per ogni utente
      Set<Post> userPostSet = userPostMap.get(username);
      for(Post post: userPostSet){ // itero per ogni post dell'utente
        for(String word: words){ // controllo tutte le parole
          if( post.doesContainWord(word) == true ){ // controllo se il post contiene la parola
            postList.add(post); // allora aggiungo il post alla lista
          }
        }
      }
    }
    return postList;
  }

  /*
    Aggiunge un utente al social; l'inserimento comporta:
    -Inserimento nella mappa dei post.
    -Inserimento nella mappa dei followers
    -Inserimento nella mappa userFollowsMap
    L'inserimento avviene solo se la stringa username non è null e se l'utente
    non è già presente nel sistema.

    REQUIRES: newUser != null && newUser not in dom(userPosts)
    THROWS: NullPointerException se newUser == null
            UserAlreadyExistException se newUser in dom(userPosts)
    MODIFIES: this
    EFFECTS:  dom(userPosts_post) = dom(userPosts_pre) U {newUser} &&
              userPosts_post(newUser) = {} &&
              dom(userFollowers_post) = dom(userFollowers_pre) U {newUser} &&
              userFollowers_post(newUser) = {} &&
              dom(userFollows_post) = dom(userFollows_pre) U {newUser} &&
              userFollows_post(newUser) = {}
  */
  public void addUser(String newUser) throws NullPointerException, UserAlreadyExistException{
      if( newUser == null ){ // se l'username utente è null
        throw new NullPointerException();
      }
      if( this.userPostMap.containsKey(newUser) == true ){
        throw new UserAlreadyExistException(USER_ALREADY_EXIST_EXCEPTION); // l'utente già esiste
      }
      else{ // lo aggiungo alle strutture
        this.userPostMap.put(newUser, new TreeSet<Post>());
        this.userFollowersMap.put(newUser, new HashSet<String>());
        this.userFollowsMap.put(newUser, new HashSet<String>());
      }
  }

  /*
    Rimuove un utente dal social; l'eliminazione comporta:
    -Aggiornamento della mappa dei post, con rimozione della chiave dell'utente eliminato
     e rimozione dai post di tutti i likes da lui messi
    -Rimozione della chiave dell'utente dalla mappa dei followers
    -Rimozione di tutte le occorrenze dell'username dell'utente eliminato dalla mappa userFollowsMap
    La rimozione avviene solo se la stringa username non è null e se l'utente
    è presente nel sistema.

    REQUIRES: username != null && username in dom(userPosts)
    THROWS: NullPointerException se username == null
            UserNotExistException se username not in dom(userPosts)
    MODIFIES: this
    EFFECTS: dom(userPosts_post) = dom(userPosts_pre) \ {newUser} &&
             dom(userFollowers_post) = dom(userFollowers_pre) \ {newUser} &&
             dom(userFollows_post) = dom(userFollows_pre) \ {newUser} &&
             forall i. i in dom(userPosts_post) =>
              (forall j. j in userPosts_post(i) => j.getUsersLike().contains(username) == false) &&
             forall i. i in dom(userFollows) => username not in userFollows(i) &&
             forall i. i in dom(userFollowers) => username not in userFollowers(i)
  */
  public void removeUser(String username) throws NullPointerException, UserNotExistException{
    if( username == null ){
      throw new NullPointerException();
    }
    if( userPostMap.containsKey(username) == true ){
      // rimozione delle occorrenze dell'username dalla mappa userFollowsMap
      userFollowsMap.remove(username);
      for(Set<String> set: userFollowsMap.values()){
        set.remove(username);
      }
      // rimozione dei post dell'utente e dei likes cha ha messo ai post
      userPostMap.remove(username); // rimozione dei post dell'utente
      for(Set<Post> postsSet: userPostMap.values()){
        for(Post post: postsSet){
          post.removeLike(username); // rimuovo un suo eventuale like dal post
        }
      }
      // rimozione dell'utente dalle liste di followers
      userFollowersMap.remove(username); // rimozione della lista followers dell'utente
      for(Set<String> set: userFollowersMap.values()){
        set.remove(username);
      }
    }
    else{
      throw new UserNotExistException(USER_NOT_EXIST_EXCEPTION);
    }
  }

  /*
    Aggiunge un post alla rete ed aggiorna:
    - la mappa degli utenti che un utente segue.
    - la mappa dei followers dell'autore
    - il set dei post pubblicati dall'autore, aggiungendo il post

    REQUIRES: post != null && post.getId() >= 0  && forall i. post.getUsersLike().contains(i) => i in dom(userPosts) &&
              post.getAuthor() in dom(userPosts) &&
              forall j. j in dom(userPosts) => ( not exist p. p in userPosts(j) => post.equals(p) )
    MODIFIES: this
    THROWS: NullPointerException se post == null
            UserNotExistException se exist i. post.getUsersLike().contains(i) => i not in dom(userPosts)
                                     || post.getAuthor not in dom(userPosts)
            PostAlreadyExistException se exist i. i in dom(userPosts) t.c. post in userPosts(i)
            NegativeIdException se post.getId() < 0
    EFFECTS: Sia auth = post.getAuthor()
             userPosts(auth)_post = userPosts(auth)_pre U {post} &&
             forall i. post.getUsersLike().contains(i) => userFollowers(auth)_post = userFollowers(auth)_pre U post.getUsersLike() &&
             forall i. post.getUsersLike().contains(i) => userFollows(i)_post = userFollows(i)_pre U {auth}
  */
  public void addPost(Post post) throws NullPointerException, UserNotExistException, PostAlreadyExistException, NegativeIdException{
    if( post == null ){
      throw new NullPointerException();
    }
    if( post.getId() < 0 ){
      throw new NegativeIdException();
    }
    // controllo che tutti gli utenti che abbiano messo like al post stiano nel social
    if( containsUser(post.getUsersLike()) == false ){
      throw new UserNotExistException("A user in the Like list doesn't exist in the system");
    }
    // controllo che il post non esiste già nel sistema (controllando l'id)
    if( containsPostById(post.getId()) == true ){
      throw new PostAlreadyExistException("A post with this id already exist in the system");
    }
    // controllo che l'autore del post esista nel sistema
    String authorUsername = post.getAuthor();
    if( this.userPostMap.containsKey(authorUsername) == false){
      throw new UserNotExistException(USER_NOT_EXIST_EXCEPTION); // l'autore del post non esiste nel social
    }

    this.userPostMap.get(authorUsername).add(new Post(post)); // Se il post non esisteva già continuo e lo inserisco
    HashSet<String> usersLikeSet = post.getUsersLike();
    userFollowersMap.get(authorUsername).addAll(usersLikeSet); // aggiungo il followers all'utente
    for(String userWhoLiked: usersLikeSet){ // inserisco nella mappa userFollowsMap
      userFollowsMap.get(userWhoLiked).add(authorUsername);
    }
  }

  /*
    La funzione restituisce un booleano vero se ogni username nella collezione
    è presente nel sistema, falso altrimenti

    REQUIRES: usernameCollection != null
    THROWS: NullPointerException se usernameCollection == null
    EFFECTS: Ritorna vero se forall i. usernameCollection.contains(i) =>
             i in dom(userPosts), falso altrimenti
  */
  public boolean containsUser(Collection<String> usernameCollection) throws NullPointerException{
    if( usernameCollection == null){
      throw new NullPointerException();
    }
    return userPostMap.keySet().containsAll(usernameCollection);
  }

  /*
    La funzione restituisce un booleano che mi dice la presenza o meno dello
    username nel social

    REQUIRES: username != null
    THROWS: NullPointerException se username == null
    EFFECTS: Ritorna vero se username in dom(usersPost), falso altrimenti
  */
  public boolean containsUser(String username) throws NullPointerException{
    if( username == null ){
      throw new NullPointerException();
    }
    return this.userPostMap.containsKey(username);
  }

  /*
    La funzione restituisce vero se nel social è presente un post con l'id
    specificato come parametro, falso altrimenti

    REQUIRES: idPost >= 0
    THROWS: NegativeIdException se idPost < 0
    EFFECTS: Ritorna vero se exist i. i in dom(userPosts) => exist j. j in userPosts(i) && j.getId() == idPost,
             falso altrimenti
  */
  public boolean containsPostById(int idPost) throws NegativeIdException{
    if( idPost < 0){
      throw new NegativeIdException();
    }
    boolean flag = false;
    Iterator usersIterator = userPostMap.keySet().iterator(); // iteratore degli utenti salvati in userPostMap
    while(usersIterator.hasNext() && flag == false){
      String key = (String) usersIterator.next();
      Set<Post> postSet = userPostMap.get(key);
      Iterator postsIterator = postSet.iterator();
      while( postsIterator.hasNext() && flag == false ){ // itero i post in ognuno dei set
        Post p = (Post) postsIterator.next();
        if( p.getId() == idPost ){
          flag = true;
        }
      }
    }
    return flag;
  }

  /*
    Restituisce una deep copy della rappresentazione di userFollows
  */
  public Map<String, Set<String>> getUserFollowsMap(){
    Map<String, Set<String>> tmp = new HashMap<>();
    for(String key: this.userFollowsMap.keySet()){ // itero le chiavi della mappa
      tmp.put(key, new HashSet<String>(this.userFollowsMap.get(key))); // inserisco ogni coppia (chiave, valore) nella nuova mappa
    }
    return tmp;
  }

  /*
    Restituisce una deep copy della rappresentazione di userPosts
  */
  public Map<String, Set<Post>> getUserPostMap(){
    Map<String, Set<Post>> tmpMap = new HashMap<>(); // inizializzo una mappa vuota
    for(String key: this.userPostMap.keySet()){ // itero le chiavi
      Set<Post> tmpSet = new TreeSet<>(); // per ogni chiave, inizializzo un nuovo set vuoto
      for(Post p: this.userPostMap.get(key)){ // itero tutti i post
        tmpSet.add(new Post(p)); // creo una deep copy del post
      }
      tmpMap.put(key, tmpSet);
    }
    return tmpMap;
  }

  /*
    Restituisce una deep copy della rappresentazione di userFollowers
  */
  public Map<String, Set<String>> getUserFollowersMap(){
    Map<String, Set<String>> tmp = new HashMap<>();
    for(String key: this.userFollowersMap.keySet()){ // itero le chiavi della mappa
      tmp.put(key, new HashSet<String>(this.userFollowersMap.get(key))); // inserisco ogni coppia (chiave, valore) nella nuova mappa
    }
    return tmp;
  }

  /*
    EFFECTS: Il metodo restituisce una rappresentazione testuale di this
  */
  public String toString(){
    return String.format("%s\n%s\n%s", userFollowsMap.toString(), userPostMap.toString(), userFollowersMap.toString());
  }

}
