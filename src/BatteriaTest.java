import java.util.*;
import java.sql.Timestamp;

public class BatteriaTest{
  public static void main(String[] args) throws Exception{
    // INIZIALIZZAZIONI NOME UTENTI
    String username1 = "Theozz";
    String username2 = "bendico765";
    String username3 = "Jomsviking";
    String username4 = "TheGoodStrau";
    String username5 = "jfet97";
    String username6 = "LordJb";
    String username7 = "Gervaz";
    // INIZIALIZZAZIONE TESTI DI ALCUNI POST
    char txt1[] = "Buona notte campioni.".toCharArray();
    char txt2[] = "prova".toCharArray();
    char txt3[] = "Un testo complesso".toCharArray();
    char txt4[] = new char[0];
    char txt5[] = new char[10];
    char txt6[] = "Un altro testo di prova".toCharArray();
    char errorTxt1[] = null;
    char errorTxt2[] = new char[141];
    // INIZIALIZZAZIONE ALCUNI POST
    Post p1 = new Post(username1, txt1, new Timestamp(392929391), new HashSet<String>());
    Post p2 = new Post(username2, txt2, new Timestamp(29328912), new HashSet<String>());
    Post p3 = new Post(username3, txt3, new Timestamp(293920319), new HashSet<String>());
    Post p4 = new Post(username4, txt4, new Timestamp(1929193), new HashSet<String>());
    Post p5 = new Post(username1, txt5, new Timestamp(928391919), new HashSet<String>());
    Post p8 = new Post(username6, txt6, new Timestamp(484929), new HashSet<String>());
    Post p9 = new Post(username7, txt1, new Timestamp(4930291), new HashSet<String>());
    boolean repInvPosts = p1.checkRep() && p2.checkRep() && p3.checkRep() && p4.checkRep()
    && p5.checkRep() && p8.checkRep() && p9.checkRep();
    System.out.println("Le rep. inv. dei post sono tutte valide: " + repInvPosts);
    try{
      System.out.println("\nProvo a creare un post con testo null");
      Post p6 = new Post(username1, errorTxt1, new Timestamp(928391919), new HashSet<String>()); // Lancia NullPointerException
    }
    catch(Exception e){System.out.println(e);}
    try{
      System.out.println("\nProvo a creare un post con testo superiore alla dim. massima consentita");
      Post p7  =new Post(username1, errorTxt2, new Timestamp(928391919), new HashSet<String>()); // Lancia TextTooLongException
    }
    catch(Exception e){System.out.println(e);}
    // Inserimento di alcuni likes nei post
    try{
      System.out.println("\nProvo a far mettere ad un utente like ad un suo post");
      p1.addLike(username1); // Lancia AuthorCantLikeHimselfException
    }
    catch(Exception e){System.out.println(e);}
    p1.addLike(username2);
    p1.addLike(username3);
    p2.addLike(username5);
    p2.addLike(username3);
    p3.addLike(username1);
    p4.addLike(username1);
    p4.addLike(username2);
    p4.addLike(username3);
    p5.addLike(username5);
    p8.addLike(username4);
    p8.addLike(username5);
    // Creazione del social ed inserimento di utenti e post inseriti fino ad ora
    SocialNetwork social = new SocialNetwork();
    //System.out.println("\nIl social inizializzato \"vuoto\" rispetta la rep. inv: " + social.checkRep());
    social.addUser(username1);
    social.addUser(username2);
    social.addUser(username3);
    social.addUser(username4);
    social.addUser(username5);
    social.addUser(username6);
    System.out.println("\nIl rep. inv. è vero dopo l'inserimento degli utenti: " + social.checkRep());
    try{
      System.out.println("\nProvo ad inserire un utente che già esiste");
      social.addUser(username1); // Lancia UserAlreadyExistException
    }
    catch(Exception e){System.out.println(e);}
    social.addPost(p1);
    social.addPost(p2);
    social.addPost(p3);
    social.addPost(p4);
    social.addPost(p5);
    System.out.println("\nIl rep. inv. è vero dopo l'inserimento dei post: " + social.checkRep());
    try{
      System.out.println("\nProvo ad inserire un post il cui autore non esiste nel social");
      social.addPost(p9); // Lancia UserNotExistException
    }
    catch(Exception e){System.out.println(e);}
    try{
      System.out.println("\nProvo ad inserire un post già presente nel social");
      social.addPost(p5); // Lancia PostAlreadyExistException
    }
    catch(Exception e){System.out.println(e);}
    /*
      proviamo ad aggiungere l'utente username7, il suo post p9 e poi rimuovere
      l'utente; se il metodo di rimozione funziona correttamente, allora
      lo stato del social prima dell'inserimento (convertito in stringa con toString)
      dovrà essere uguale allo stato successivo alla rimozione
    */
	String relazioniSocialPre = social.toString();
    social.addUser(username7);
    p9.addLike(username4);
    p9.addLike(username6);
    social.addPost(p9);
    social.removeUser(username7);
    System.out.println("\nLo \"stato\" del social prima dell'inserimento è uguale "
                      + "allo stato dopo la rimozione: "
                      + relazioniSocialPre.equals(social.toString()));
    System.out.println("\nDopo queste operazione di rimozioni ed inserimenti, "
                      + "il rep. inv. è: " + social.checkRep());
    // Proviamo i metodi assegnati dal progetto
    // METODO 1: Map<String, Set<String>> guessFollowers(List<Post> ps)
    System.out.println("\nMETODO 1 - guessFollowers");
    List<Post> list = new ArrayList<>();
    list.add(p1);
    list.add(p2);
    list.add(p3);
    list.add(p4);
    list.add(p5);
    System.out.println(SocialNetwork.guessFollowers(list));
    list.add(null);
    try{
      System.out.println("\nProvo a chiamare il metodo con una lista contenente null");
      System.out.println(SocialNetwork.guessFollowers(list));
    }
    catch (Exception e){
      System.out.println(e);
    }
    list.remove(null);
    // METODO 2: List<String> influencers(Map<String, Set<String>> followers)
    System.out.println("\nMETODO 2 - influencers");
    Map<String, Set<String>> followers = new HashMap<>(); // carico la mappa che userò come parametro
    for(String key: social.getUserFollowsMap().keySet()){
      HashSet<String> tmpSet = new HashSet<>(social.getUserFollowsMap().get(key));
      followers.put(key, tmpSet );
    }
    System.out.println("Il risultato della chiamata di influencers:");
    System.out.println(SocialNetwork.influencers(followers));
    followers.put("UsernameFittizio", new HashSet<>());
    followers.get("UsernameFittizio").add(null);
    try{
      System.out.println("Provo a passare a followers una mappa che contiene "
                        + "un set contenente null al proprio interno");
      SocialNetwork.influencers(followers);
    }
    catch(Exception e){
      System.out.println(e);
    }
    // METODO 3: Set<String> getMentionedUsers()
    System.out.println("\nMETODO 3 - getMentionedUsers()");
    System.out.println("Risultato chiamata mentioned users:");
    System.out.println(social.getMentionedUsers());
    // METODO 4: Set<String> getMentionedUsers(List<Post> postList)
    System.out.println("\nMETODO 4 - getMentionedUsers(List)");
    List<Post> list2 = new LinkedList<>(list); // uso la stessa lista di post di prima...
    System.out.println("La chiamata di mentionedUsers effettuata con la lista di post salvati "
                      + "nel social produce lo stesso risultato di mentionedUsers chiamato "
                      + "senza parametri: " + SocialNetwork.getMentionedUsers(list2).equals(social.getMentionedUsers()));
    // METODO 5: List<Post>	writtenBy(String username)
    System.out.println("\nMETODO 5 - writtenBy(String)");
    System.out.println("Chiamo writtenBy chiedendo i post di username1");
    System.out.println(social.writtenBy(username1));
    System.out.println("\nLista di post di un utente che non ha pubblicato post");
    System.out.println(social.writtenBy(username6)); // lista vuota
    try{
      System.out.println("\nChiamo writtenBy usando un username che non è presente nel social");
      System.out.println(social.writtenBy(username7)); // Lancia UserNotExistException
    }
    catch(Exception e){
      System.out.println(e);
    }
    // METODO 6: List<Post>	writtenBy(List<Post> ps, String	username)
    System.out.println("\nMETODO 6 - writtenBy(List, String)");
    System.out.println("Chiamo writtenby chiedendo i post di username1 ");
    System.out.println(SocialNetwork.writtenBy(list2, username1));
    System.out.println("\n Provo a filtrare i post scegliendo un username che non esiste: "
                      + "si noti come questa volta non viene lanciata alcuna eccezione");
    System.out.println(SocialNetwork.writtenBy(list2, username7)); // questa volta non tira eccezioni!
    // METODO 7: List<Post> containing(List<String>	words)
    System.out.println("\nMETODO 7 - containing");
    List<String> listBadWords = new LinkedList<>();
    listBadWords.add("campioni");
    System.out.println("Bad words: " + listBadWords.toString());
    System.out.println(social.containing(listBadWords));
    listBadWords.clear();
    listBadWords.add("cAmPiOnI");
    System.out.println("Bad words: " + listBadWords.toString());
    System.out.println(social.containing(listBadWords));
    listBadWords.add("lesso");
    System.out.println("Bad words: " + listBadWords.toString());
    System.out.println(social.containing(listBadWords));
    listBadWords.clear();
    listBadWords.add("testo complesso");
    System.out.println("Bad words: " + listBadWords.toString());
    System.out.println(social.containing(listBadWords));
    String nullString = null;
    listBadWords.add(nullString);
    try{
      System.out.println("\nProvo ad inserire nella lista delle words una stringa null");
      System.out.println(social.containing(listBadWords)); // lancia NullPointerException
    }
    catch(Exception e){
      System.out.println(e);
    }
    // TEST DI ALCUNI METODI SECONDARI DELLA CLASSE
    // Verifico che i metodi getters delle mappe del social mi restituiscano
    // effettivamente delle deep copy, ossia che provando a modificare
    // le copie, l'oggetto iniziale non cambia
    Map<String,Set<String>> tmpCopy; // variabile temporanea d'appoggio
    tmpCopy = social.getUserFollowsMap();
    String social_pre = social.toString();
    tmpCopy.put("utenteCasuale",new HashSet<String>());
    System.out.println("\nLa modifica della copia ha cambiato lo stato interno "
                      + "del social: " + !social_pre.equals(social.toString()));
    // ALCUNI TEST DELLA SOTTO CLASSE CON I REPORT
    System.out.println("\nAlcuni test con la sottoclasse che gestisce i report");
    SocialNetworkWithReport social2 = new SocialNetworkWithReport();
    social2.addUser(username1);
    social2.addUser(username2);
    social2.addUser(username3);
    social2.addUser(username4);
    social2.addUser(username5);
    social2.addUser(username6);
    social2.addPost(p1);
    social2.addPost(p2);
    social2.addPost(p3);
    social2.addPost(p4);
    social2.addPost(p5);
    social2.reportPost(1, username2);
    social2.reportPost(1, username3);
    social2.reportPost(1, username4);
    System.out.println(social2.getReportedPostMap());
    try{
      System.out.println("\nProvo ad usare un id negativo");
      social2.reportPost(-1, username2);
    }
    catch(Exception e){
      System.out.println(e);
    }
    try{
      System.out.println("\nProvo a segnalare un post usando un utente inesistente");
      social2.reportPost(1, "nessuno");
    }
    catch(Exception e){
      System.out.println(e);
    }
    try{
      System.out.println("\nProvo a segnalare un post che non esiste");
      social2.reportPost(100, username1);
    }
    catch(Exception e){
      System.out.println(e);
    }
  }
}
