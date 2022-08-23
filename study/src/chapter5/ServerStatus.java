package chapter5;

import annotation.GuardedBy;
import annotation.ThreadSafe;

import java.util.Set;

/**
 * 락 분할 대상
 */
@ThreadSafe
public class ServerStatus {

    @GuardedBy("this") public final Set<String> users;
    @GuardedBy("this") public final Set<String> queries;

    public ServerStatus(Set<String> users, Set<String> queries) {
        this.users = users;
        this.queries = queries;
    }

    public synchronized void addUser(String u){
        users.add(u);
    }

    public synchronized void addQuery(String q){
        queries.add(q);
    }

    public synchronized void removeUser(String u){
        users.remove(u);
    }

    public synchronized void removeQuery(String q){
        queries.remove(q);
    }

}
