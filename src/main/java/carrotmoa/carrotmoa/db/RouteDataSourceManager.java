//package carrotmoa.carrotmoa.db;
//
//import org.springframework.transaction.support.TransactionSynchronization;
//import org.springframework.transaction.support.TransactionSynchronizationManager;
//
//import static carrotmoa.carrotmoa.db.RouteDataSource.DataSourceType.MASTER;
//import static carrotmoa.carrotmoa.db.RouteDataSource.DataSourceType.SLAVE;
//
//public class RouteDataSourceManager {
//
//    private static final ThreadLocal<RouteDataSource.DataSourceType> currentDataSourceName = new ThreadLocal<>();
//
//    public static RouteDataSource.DataSourceType getCurrentDataSourceName() {
//        if (currentDataSourceName.get() == null) {
//            return TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? SLAVE : MASTER;
//        }
//        return currentDataSourceName.get();
//    }
//
//    public static void setCurrentDataSourceName(RouteDataSource.DataSourceType dataSourceType) {
//        currentDataSourceName.set(dataSourceType);
//    }
//
//    public static void removeCurrentDataSourceName() {
//        currentDataSourceName.remove();
//    }
//}
