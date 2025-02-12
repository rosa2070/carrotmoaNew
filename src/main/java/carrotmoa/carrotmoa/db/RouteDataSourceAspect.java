//package carrotmoa.carrotmoa.db;
//
//import okhttp3.Route;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.stereotype.Component;
//
//import static carrotmoa.carrotmoa.db.RouteDataSource.DataSourceType.MASTER;
//import static carrotmoa.carrotmoa.db.RouteDataSource.DataSourceType.SLAVE;
//
//@Aspect
//@Component
//public class RouteDataSourceAspect {
//
//    @Before("@annotation(carrotmoa.carrotmoa.db.RouteDataSource) && @annotation(target)")
//    public void setDataSource(RouteDataSource target) throws Exception {
//        if (target.dataSourceType() == MASTER
//            || target.dataSourceType() == SLAVE) {
//            RouteDataSourceManager.setCurrentDataSourceName(target.dataSourceType());
//        } else {
//            throw new Exception("Wrong DataSource Type : Should check Exception");
//        }
//    }
//
//    @After("@annotation(carrotmoa.carrotmoa.db.RouteDataSource)")
//    public void clearDataSource() {
//        RouteDataSourceManager.removeCurrentDataSourceName();
//    }
//
//}
