package try_1.chapter5;

import annotation.GuardedBy;
import annotation.ThreadSafe;

import java.util.Set;

/**
 * 락이 분할된 ServerStatus 클래스
 */
@ThreadSafe
public class ServerStatus {

    @GuardedBy("this") public final Set<String> users;
    @GuardedBy("this") public final Set<String> queries;

    public ServerStatus(Set<String> users, Set<String> queries) {
        this.users = users;
        this.queries = queries;
    }

    public void addUser(String u){
        synchronized(users){
            users.add(u);
        }
    }

    public void addQuery(String q){
        synchronized(queries){
            queries.add(q);
        }
    }

    public void removeUser(String u){
        synchronized(users){
            users.remove(u);
        }
    }

    public void removeQuery(String q){
        synchronized(queries){
            queries.remove(q);
        }
    }

}
