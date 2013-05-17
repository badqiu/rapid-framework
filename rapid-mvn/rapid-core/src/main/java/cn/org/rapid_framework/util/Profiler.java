package cn.org.rapid_framework.util;


import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用来测试并统计线程执行时间的工具。
 *
 * Profiler.start(message),Profiler.release() 及
 * Profiler.enter(message),Profiler.release() 要成对出现
 * 
 * <pre>
 *	@Test
 *	public void test_dump() throws InterruptedException {
 *		try {
 *			Profiler.start("businessProcess");
 *
 *			Thread.sleep(1100);
 *			doSomething();
 *		} finally {
 *			Profiler.release();
 *			System.out.println(Profiler.dump());
 *		}
 *	}
 *
 *	public void doSomething() throws InterruptedException {
 *		try {
 *			Profiler.enter("doSomething");
 *
 *			// do some thing...
 *			Thread.sleep(380);
 *
 *		} finally {
 *			Profiler.release();
 *		}
 *	}
 * 
 * </pre>
 * dump()输出如下:
 * <pre>
 * [totalCost:1,484ms (selfCost:1,109ms), all:100% start:0ms ] - businessProcess
 * `---[totalCost:375ms, parent:25.3%, all:25.3% start:1,109ms ] - doSomething
 * </pre>
 * 
 * <pre>
 * totalCost: 	当前任务总耗时
 * selfCost: 	当前耗时,将子任务耗时拿开
 * parent: 		在父任务中所占的耗时比重
 * all: 		在整个任务中所占的耗时比重
 * start: 		任务开始至现在的开始时间
 * loopCount:   循环变量,用于计算TPS(每秒事务处理量Transaction Per Second)
 * TPS: 		每秒事务处理量Transaction Per Second
 * resultSize:  方法执行的结果集大小
 * </pre>
 * 
 * @author badqiu
 */
public final class Profiler {
	
	private static final ThreadLocal<Step> stepThreadLocal = new ThreadLocal<Step>();

	/**
     * 开始计时。
     */
    public static void start() {
        start((String) null);
    }

    /**
     * 开始计时。
     *
     * @param message 第一个step的信息
     */
    public static void start(String message) {
        start(message,0);
    }

    /**
     * 开始计时。
     *
     * @param loopCount   循环变量,用于计算TPS(每秒事务处理量Transaction Per Second)使用
     */
    public static void start(long loopCount) {
        start(null,loopCount);
    }
    
    /**
     * 开始计时。
     *
     * @param message 第一个step的信息
     * @param loopCount   循环变量,用于计算TPS(每秒事务处理量Transaction Per Second)使用
     */
    public static void start(String message,long loopCount) {
        stepThreadLocal.set(new Step(message, null, null,loopCount));
    }
    
    /**
     * 清除计时器。
     * 
     * <p>
     * 清除以后必须再次调用<code>start</code>方可重新计时。
     * </p>
     */
    public static void reset() {
        stepThreadLocal.set(null);
    }

    /**
     * 开始一个新的step，并计时。
     *
     * @param message 新step的信息
     */
    public static void enter(String message) {
    	enter(message,0);
    }

    /**
     * 开始一个新的step，并计时。
     *
     * @param message 新step的信息
     * @param loopCount   循环变量,用于计算TPS(每秒事务处理量Transaction Per Second)
     */
    public static void enter(String message,long loopCount) {
        Step currentStep = getCurrentStep();

        if (currentStep != null) {
            currentStep.enterSubStep(message,loopCount);
        }
    }
    
    /**
     * 结束最近的一个step，记录结束时间。
     */
    public static void release() {
    	release(0);
    }

    /**
     * 结束最近的一个step，记录结束时间。
     * @param loopCount   循环变量,用于计算TPS(每秒事务处理量Transaction Per Second)
     */
    public static void release(long loopCount) {
    	release(null,loopCount,0);
    }
    
    /**
     * 结束最近的一个step，记录结束时间。
     * @param loopCount   循环变量,用于计算TPS(每秒事务处理量Transaction Per Second)
     */
    public static void release(long loopCount,int resultSize) {
    	release(null,loopCount,resultSize);
    }

    /**
     * 结束最近的一个step，记录结束时间。
     * @param e 异常，得到方法执行的异常
     */
    public static void release(Throwable e) {
    	release(e,0,0);
    }
    
    /**
     * 结束最近的一个step，记录结束时间。
     * @param loopCount   循环变量,用于计算TPS(每秒事务处理量Transaction Per Second)
     * @param e 异常，得到方法执行的异常
     */
    public static void release(Throwable e,long loopCount) {
    	release(e,loopCount,0);
    }
    
    /**
     * 结束最近的一个step，记录结束时间。
     * @param loopCount   循环变量,用于计算TPS(每秒事务处理量Transaction Per Second)
     * @param e 异常，得到方法执行的异常
     */
    public static void release(Throwable e,long loopCount,int resultSize) {
        Step currentStep = getCurrentStep();

        if (currentStep != null) {
            currentStep.release(e,loopCount,resultSize);
        }
    }
    
    /**
     * 结束最近的一个step，记录结束时间。
     * @param loopCount   循环变量,用于计算TPS(每秒事务处理量Transaction Per Second)
     * @param e 异常，得到方法执行的异常
     * @param resultSizeObject 用于得到resultSize的object对象
     */
    public static void release(Throwable e,long loopCount,Object resultSizeObject) {
        Step currentStep = getCurrentStep();

        if (currentStep != null) {
            currentStep.release(e,loopCount,ObjectUtils.getSize(resultSizeObject));
        }
    }
    
    /**
     * 取得耗费的总时间。
     *
     * @return 耗费的总时间，如果未开始计时，则返回<code>-1</code>
     */
    public static long getDuration() {
        Step step = (Step) stepThreadLocal.get();

        if (step != null) {
            return step.getDuration();
        } else {
            return -1;
        }
    }

    /**
     * 打印列出所有的step。
     *
     * @return 列出所有step，并统计各自所占用的时间
     */
    public static String printDump() {
        String dump = dump("", "");
        if(dump != null && !dump.isEmpty()) {
        	System.out.println(dump);
        }
        return dump;
    }
    
    /**
     * 列出所有的step。
     *
     * @return 列出所有step，并统计各自所占用的时间
     */
    public static String dump() {
        return dump("", "");
    }

    /**
     * 列出所有的step。
     *
     * @param prefix 前缀
     *
     * @return 列出所有step，并统计各自所占用的时间
     */
    public static String dump(String prefix) {
        return dump(prefix, prefix);
    }

    /**
     * 列出所有的step。
     *
     * @param prefix1 首行前缀
     * @param prefix2 后续行前缀
     *
     * @return 列出所有step，并统计各自所占用的时间
     */
    public static String dump(String prefix1, String prefix2) {
        Step step = (Step) stepThreadLocal.get();

        if (step != null) {
            return step.toString(prefix1, prefix2);
        } else {
            return "";
        }
    }

    /**
     * 取得第一个step。
     *
     * @return 第一个step，如果不存在，则返回<code>null</code>
     */
    public static Step getStep() {
        return (Step) stepThreadLocal.get();
    }

    /**
     * 取得最近的一个step。
     *
     * @return 最近的一个step，如果不存在，则返回<code>null</code>
     */
    private static Step getCurrentStep() {
        Step subStep = (Step) stepThreadLocal.get();
        Step step = null;

        if (subStep != null) {
            do {
                step    = subStep;
                subStep = step.getUnreleasedStep();
            } while (subStep != null);
        }

        return step;
    }

    /**
     * 代表一个计时单元。
     */
    public static final class Step {
        private final List<Step>   subStepList  = new ArrayList<Step>(4);
        private final String message;
        private final Step  parentStep;
        private final Step  firstStep;
        private final long   baseTime;
        private final long   startTime;
        private long         endTime;
        
        private long 		 loopCount;
        private Throwable exception;
        private int resultSize;
        
        /**
         * 创建一个新的step。
         *
         * @param message step的信息，可以是<code>null</code>
         * @param parentStep 父step，可以是<code>null</code>
         * @param firstStep 第一个step，可以是<code>null</code>
         */
        private Step(String message, Step parentStep, Step firstStep,long loopCount) {
            this.message     = message;
            this.startTime   = System.currentTimeMillis();
            this.parentStep = parentStep;
            this.firstStep  = (Step) firstStep == null ? this : firstStep;
            this.baseTime    = (firstStep == null) ? 0
                                                    : firstStep.startTime;
            this.loopCount = loopCount;
        }

		/**
         * 取得step的信息。
         */
        public String getMessage() {
            return message == null || message.isEmpty() ? null : message;
        }
        
        /**
         * 得到开始时间
         * @return
         */
        public long getStartTime() {
			return startTime;
		}
        
        /**
         * 得到父step
         * @return
         */
		public Step getParentStep() {
			return parentStep;
		}

		/**
         * 取得step相对于第一个step的起始时间。
         *
         * @return 相对起始时间
         */
        public long getStartCostTime() {
            return (baseTime > 0) ? (startTime - baseTime)
                                  : 0;
        }

        /**
         * 取得step相对于第一个step的结束时间。
         *
         * @return 相对结束时间，如果step还未结束，则返回<code>-1</code>
         */
        public long getEndCostTime() {
            if (endTime < baseTime) {
                return -1;
            } else {
                return endTime - baseTime;
            }
        }

        /**
         * 取得step持续的时间。
         *
         * @return step持续的时间，如果step还未结束，则返回<code>-1</code>
         */
        public long getDuration() {
            if (endTime < startTime) {
                return -1;
            } else {
                return endTime - startTime;
            }
        }
        
        /**
         * 得到循环执行次数，用于计算TPS使用
         * @return
         */
        public long getLoopCount() {
			return loopCount;
		}

        /**
         * 得到这个步骤的执行的异常
         * @return
         */
		public Throwable getException() {
			return exception;
		}

		/**
         * 取得step自身所用的时间，即总时间减去所有子step所用的时间。
         *
         * @return step自身所用的时间，如果step还未结束，则返回<code>-1</code>
         */
        public long getDurationOfSelf() {
            long duration = getDuration();

            if (duration < 0) {
                return -1;
            } else if (subStepList.isEmpty()) {
                return duration;
            } else {
                for (int i = 0; i < subStepList.size(); i++) {
                    Step subStep = (Step) subStepList.get(i);

                    duration -= subStep.getDuration();
                }

                if (duration < 0) {
                    return -1;
                } else {
                    return duration;
                }
            }
        }

        /**
         * 取得当前step在父step中所占的时间百分比。
         *
         * @return 百分比
         */
        public double getPecentage() {
            double parentDuration = 0;
            double duration = getDuration();

            if ((parentStep != null) && parentStep.isReleased()) {
                parentDuration = parentStep.getDuration();
            }

            if ((duration > 0) && (parentDuration > 0)) {
                return duration / parentDuration;
            } else {
                return 0;
            }
        }

        /**
         * 取得当前step在第一个step中所占的时间百分比。
         *
         * @return 百分比
         */
        public double getPecentageOfAll() {
            double firstDuration = 0;
            double duration = getDuration();

            if ((firstStep != null) && firstStep.isReleased()) {
                firstDuration = firstStep.getDuration();
            }

            if ((duration > 0) && (firstDuration > 0)) {
                return duration / firstDuration;
            } else {
                return 0;
            }
        }
        
        /**
         * 得到TPS(每秒事务处理量Transaction Per Second)
         * @return
         */
        public int getTps() {
        	if(loopCount > 0 && getDuration() > 0) {
        		return (int)(loopCount * 1000 / getDuration());
        	}else {
        		return 0;
        	}
        }

        /**
         * 取得所有子stepList。
         *
         * @return 所有子stepList的列表（不可更改）
         */
        public List<Step> getSubStepList() {
            return Collections.unmodifiableList(subStepList);
        }
        
        public int getResultSize() {
			return resultSize;
		}

		/**
         * 结束当前step，并记录结束时间。
         */
        private void release(Throwable e,long loopCount,int resultSize) {
        	if(loopCount > 0) {
        		this.loopCount = loopCount;
        	}
        	if(e != null) {
        		exception = e;
        	}
        	if(resultSize > 0) {
        		this.resultSize = resultSize;
        	}
        	endTime = System.currentTimeMillis();
		}
        
        /**
         * 判断当前step是否结束。
         *
         * @return 如果step已经结束，则返回<code>true</code>
         */
        private boolean isReleased() {
            return endTime > 0;
        }

        /**
         * 创建一个新的子step。
         *
         * @param message 子step的信息
         */
        private void enterSubStep(String message,long loopCount) {
            Step subStep = new Step(message, this, firstStep,loopCount);
            subStepList.add(subStep);
        }

        /**
         * 取得未结束的子step。
         *
         * @return 未结束的子step，如果没有子step，或所有step均已结束，则返回<code>null</code>
         */
        private Step getUnreleasedStep() {
            Step subStep = null;

            if (!subStepList.isEmpty()) {
                subStep = (Step) subStepList.get(subStepList.size() - 1);

                if (subStep.isReleased()) {
                    subStep = null;
                }
            }

            return subStep;
        }

        /**
         * 将step转换成字符串的表示。
         *
         * @return 字符串表示的step
         */
        public String toString() {
            return toString("", "");
        }

        /**
         * 将step转换成字符串的表示。
         *
         * @param prefix1 首行前缀
         * @param prefix2 后续行前缀
         *
         * @return 字符串表示的step
         */
        private String toString(String prefix1, String prefix2) {
        	StringBuilder buffer = new StringBuilder();

            toString(buffer, prefix1, prefix2);

            return buffer.toString();
        }

        /**
         * 将step转换成字符串的表示。
         *
         * @param buffer 字符串buffer
         * @param prefix1 首行前缀
         * @param prefix2 后续行前缀
         */
        DecimalFormat pecentageFormat = new DecimalFormat("##.#%");
        DecimalFormat numberFormat = new DecimalFormat("##,###,###");
        private void toString(StringBuilder buffer, String prefix1, String prefix2) {
            buffer.append(prefix1);

            if (isReleased()) {
                buffer.append("[totalCost:"+numberFormat.format(getDuration())+"ms");

                if ((getDurationOfSelf() > 0) && (getDurationOfSelf() != getDuration())) {
                	buffer.append(" (selfCost:"+numberFormat.format(getDurationOfSelf())+"ms)");
                }

                if (getPecentage() > 0) {
                	buffer.append(", parent:").append(pecentageFormat.format(getPecentage()));
                }

                if (getPecentageOfAll() > 0) {
                	buffer.append(", all:").append(pecentageFormat.format(getPecentageOfAll()));
                }
                if(loopCount > 0) {
                	buffer.append(", loopCount:").append(numberFormat.format(getLoopCount()));
                	buffer.append(", TPS:").append(numberFormat.format(getTps()));
                }
                if(resultSize > 0) {
                	buffer.append(", resultSize:").append(numberFormat.format(getResultSize()));
                }
                buffer.append(" start:"+numberFormat.format(getStartCostTime())+"ms ");
                if(getException() != null) {
                	buffer.append(", "+ getException().getClass().getSimpleName());
                }
                buffer.append("]");
            } else {
            	buffer.append("[UNRELEASED start:"+numberFormat.format(getStartCostTime())+"]ms");
            }
            
            if (getMessage() != null) {
            	buffer.append(" - ").append(getMessage());
            }
            
            for (int i = 0; i < subStepList.size(); i++) {
                Step subStep = (Step) subStepList.get(i);

                buffer.append('\n');

                if (i == (subStepList.size() - 1)) {
                    subStep.toString(buffer, prefix2 + "`---", prefix2 + "    "); // 最后一项
                } else if (i == 0) {
                    subStep.toString(buffer, prefix2 + "+---", prefix2 + "|   "); // 第一项
                } else {
                    subStep.toString(buffer, prefix2 + "+---", prefix2 + "|   "); // 中间项
                }
            }
        }
        
        
    }


}
