package try_2.ch5;


import java.util.*;
import java.util.concurrent.*;

/**
 * 제한된 시간 안에 여행 관련 입찰 정보를 가져오도록 요청하는 코드
 */
public class QuoteTask implements Callable<TravelQuote> {
    private final TravelCompany company;
    private final TravelInfo travelInfo;
    private final ExecutorService exec = Executors.newFixedThreadPool(10);

    public QuoteTask(TravelCompany company, TravelInfo travelInfo) {
        this.company = company;
        this.travelInfo = travelInfo;
    }

    public TravelQuote call() throws Exception {
        return company.solicitQuote(travelInfo);
    }

    public List<TravelQuote> getRankedTravelQuotes(
            TravelInfo travelInfo, Set<TravelCompany> companies,
            Comparator<TravelQuote> ranking, long time, TimeUnit unit)
            throws InterruptedException {
        List<QuoteTask> tasks = new ArrayList<>();

        for(TravelCompany company : companies){
            tasks.add(new QuoteTask(company, travelInfo));
        }

        List<Future<TravelQuote>> futures =
                exec.invokeAll(tasks, time, unit);

        List<TravelQuote> quotes = new ArrayList<>(tasks.size());
        Iterator<QuoteTask> taskIter = tasks.iterator();

        for(Future<TravelQuote> f : futures){
            QuoteTask task = taskIter.next();

            try {
                quotes.add(f.get());
            } catch (ExecutionException e) {
                quotes.add(task.getFailureQuote(e.getCause()));
            } catch (CancellationException e){
                quotes.add(task.getTimeoutQuote(e));
            }
        }

        Collections.sort(quotes, ranking);
        return quotes;
    }

    private TravelQuote getFailureQuote(Throwable cause) {
        return null;
    }

    private TravelQuote getTimeoutQuote(CancellationException e) {
        return null;
    }

}
