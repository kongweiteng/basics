package com.enn.energy.passage.job;

import com.enn.energy.passage.service.ISteamFeesTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by sl
 * User: sl
 * Date: 2018/6/14
 * Time: 上午10:54
 */
@Component
public class SteamFeesJobs {

    private static Logger logger = LoggerFactory.getLogger(SteamFeesJobs.class);

    @Autowired
    private ISteamFeesTaskService steamFeesTaskService;

    //@Scheduled(cron = "0 0 3 * * ?")
    public void steamFeesTaskForHour() {
        try {
            logger.info(">>>>>>>>>>>>>>>>> SteamFeesTask start <<<<<<<<<<<<<<<<<<");

            steamFeesTaskService.steamFeesTaskForHour();
            logger.info(">>>>>>>>>>>>>>>>> SteamFeesTask end <<<<<<<<<<<<<<<<<<");
        } catch (Exception e) {
            logger.error("SteamFeesTask - SteamFeesTaskForHour error: {}", e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     *
     * <用汽-天(前一天)-计算><每天凌晨3点执行一次：0 0 3 * * ?>
     *
     * @create：2018/6/18 下午2:28
     * @author：sl
     * @param
     * @return void
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void steamFeesJobForDay() {
        try {
            logger.info(">>>>>>>>>>>>>>>>> steamFeesJobForDay start <<<<<<<<<<<<<<<<<<");

            steamFeesTaskService.steamFeesJobForDay();
            logger.info(">>>>>>>>>>>>>>>>> steamFeesJobForDay end <<<<<<<<<<<<<<<<<<");
        } catch (Exception e) {
            logger.error("SteamFeesJobs - steamFeesJobForDay error: {}", e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * <用汽-月(上个月)-计算><每月1号凌晨1点执行一次：0 0 1 1 * ?>
     *
     * @param
     * @return void
     * @create：2018/6/18 下午2:27
     * @author：sl
     */
    @Scheduled(cron = "0 0 1 1 * ?")
    public void steamFeesJobForMonth() {
        try {
            logger.info(">>>>>>>>>>>>>>>>> steamFeesJobForMonth start <<<<<<<<<<<<<<<<<<");

            steamFeesTaskService.steamFeesJobForMonth();
            logger.info(">>>>>>>>>>>>>>>>> steamFeesJobForMonth end <<<<<<<<<<<<<<<<<<");
        } catch (Exception e) {
            logger.error("SteamFeesJobs - steamFeesJobForMonth error: {}", e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * <用汽-上月同比><每月1号凌晨2点执行一次：0 0 2 1 * ?>
     *
     * @param
     * @return void
     * @create：2018/6/18 下午2:27
     * @author：sl
     */
    @Scheduled(cron = "0 0 2 1 * ?")
    public void lastMonthPercent() {
        try {
            logger.info(">>>>>>>>>>>>>>>>> steamPercentJobForMonth start <<<<<<<<<<<<<<<<<<");

            steamFeesTaskService.lastMonthPercent();
            logger.info(">>>>>>>>>>>>>>>>> steamPercentJobForMonth end <<<<<<<<<<<<<<<<<<");
        } catch (Exception e) {
            logger.error("SteamFeesJobs - steamPercentJobForMonth error: {}", e.getMessage());
            e.printStackTrace();
        }

    }
    /**
     * <用汽-同期环比><每月1号凌晨2点30分执行一次：0 30 2 1 * ?>
     *
     * @param
     * @return void
     * @create：2018/6/18 下午2:27
     * @author：sl
     */
    @Scheduled(cron = "0 30 2 1 * ?")
    public void samePeriodPercent() {
        try {
            logger.info(">>>>>>>>>>>>>>>>> steamPercentJobForMonth start <<<<<<<<<<<<<<<<<<");

            steamFeesTaskService.samePeriodPercent();
            logger.info(">>>>>>>>>>>>>>>>> steamPercentJobForMonth end <<<<<<<<<<<<<<<<<<");
        } catch (Exception e) {
            logger.error("SteamFeesJobs - steamPercentJobForMonth error: {}", e.getMessage());
            e.printStackTrace();
        }

    }

}
