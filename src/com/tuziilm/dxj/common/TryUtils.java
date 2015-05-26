package com.tuziilm.dxj.common;

import com.tuziilm.dxj.exception.NeedRetryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * try func with times
 * @author <a href="tuziilm@163.com">tuziilm</a>
 */
public final class TryUtils {
    private final static Logger log=LoggerFactory.getLogger(TryUtils.class);
    public final static int MAX_TRY_TIMES=3;

    public static interface Func<R>{
        R run();
        void callbackWhenRetry(int times, NeedRetryException e);
        void callbackWhenFail(int times, NeedRetryException e);
    }
    public static <R> R doTry(Func<R> func){
        return doTry(func, MAX_TRY_TIMES);
    }
    public static <R> R doTry(Func<R> func, int times){
        if(times<1)
            times=1;
        R result=null;
        for(int i=1;i<=times;i++){
            try{
                result = func.run();
                break;
            }catch(NeedRetryException e){
                if(i < times){
                    func.callbackWhenRetry(i, e);
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e1) {
                        log.error("thread interrupted.", e1);
                    }
                    continue;
                }else{
                    func.callbackWhenFail(i, e);
                    break;
                }
            }
        }
        return result;
    }
}
