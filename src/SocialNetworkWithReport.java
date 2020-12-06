import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

public class SocialNetworkWithReport extends SocialNetwork{
  private Map<Integer, Set<String>> reportedPostMap;
  // reportedPost(idPost) -> {username degli utenti che l'hanno segnalato}

  /*
    Inizializza il social
  */
  public SocialNetworkWithReport(){
    super();
    this.reportedPostMap = new HashMap<>();
  }

  /*
    La funzione permette di segnalare un post, aggiungendo al set associato alla
    chiave idPost il valore username. Restituisce vero se l'inserimento è
    andato a buon fine, falso altrimenti

    REQUIRES: idPost >= 0 && super.containsPostById(idPost) && username != null
    THROWS: NullPointerException se username == null
            NegativeIdException se idPost < 0
            PostDoesNotExist se il post identificato dell'id non è contenuto nel social
            UserNotExistException se l'utente identificato da username non è contenuto nel social
    EFFECTS: se l'idPost è già tra le chiavi di reportedPostMap allora username
             viene aggiunto al set associato a idPost e viene ritornato vero, altrimenti
             viene restituito falso.
  */
  public boolean reportPost(int idPost, String username) throws NullPointerException, NegativeIdException, PostDoesNotExist, UserNotExistException{
    if( username == null ){
      throw new NullPointerException();
    }
    if( idPost < 0 ){
      throw new NegativeIdException("The post id is negative");
    }
    if( super.containsUser(username) == false ){
      throw new UserNotExistException("The user identified by the username doesn't exist in the system");
    }
    if( super.containsPostById(idPost) == false){
      throw new PostDoesNotExist("The post identified by the id doesn't exist");
    }
    if( reportedPostMap.containsKey(idPost) == false ){
      reportedPostMap.put(idPost, new HashSet<String>());
    }
    return reportedPostMap.get(idPost).add(username);
  }

  public Map<Integer,Set<String>> getReportedPostMap(){
    return new HashMap<>(this.reportedPostMap);
  }
}
