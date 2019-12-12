package com.dxz.bigdata.jmx.constant;

/**
 * Created by yanlishuai on 2019-11-22
 */
public class ItemConstants {
    //HBase
    //存活的regionServer个数
    public static final String NUM_REGION_SERVERS = "numRegionServers";
    //每个regionServer平均region数目
    public static final String AVERAGE_LOAD = "averageLoad";
    //所有regionServer总请求数量
    public static final String CLUSTER_REQUESTS = "clusterRequests";
    //The number of regions in transition
    public static final String RIT_COUNT = "ritCount";
    //The number of regions that have been in transition longer than a threshold time (default: 60 seconds)
    public static final String RIT_COUNT_OVER_THRESHOLD = "ritCountOverThreshold";
    //RegionServer上Region的数量
    public static final String REGION_COUNT = "regionCount";
    //RegionServer上HFile的数量
    public static final String STORE_FILE_COUNT = "storeFileCount";
    //RegionServer上HFile的数量
    public static final String STORE_FILE_SIZE = "storeFileSize";
    //RegionServer上Hlog文件数量
    public static final String HLOG_FILE_COUNT = "hlogFileCount";
    //RegionServer累计请求数
    public static final String TOTAL_REQUEST_COUNT = "totalRequestCount";
    //RegionServer累计读请求数
    public static final String READ_REQUEST_COUNT = "readRequestCount";
    //RegionServer累计写请求数
    public static final String WRITE_REQUEST_COUNT = "writeRequestCount";
    //RegionServer上开启的RPC连接数
    public static final String NUM_OPEN_CONNECTIONS = "numOpenConnections";
    //RegionServer上活跃的请求队列Handler数量
    public static final String NUM_ACTIVE_HANDLER = "numActiveHandler";
    //RegionServer上flush队列长度
    public static final String FLUSH_QUEUE_LENGTH = "flushQueueLength";
    //RegionServer上Compaction队列长度
    public static final String COMPACTION_QUEUE_LENGTH = "compactionQueueLength";
    //RegionServer上当前GC时长
    public static final String GC_TIME_MILLIS = "GcTimeMillis";
    //RegionServer上新生代GC时长
    public static final String GC_TIME_MILLIS_PAR_NEW = "GcTimeMillisParNew";
    //RegionServer上老年代GC时间
    public static final String GC_TIME_MILLIS_CONCURRENT_MARK_SWEEP = "GcTimeMillisConcurrentMarkSweep";
    //RegionServer上MemStore的总大小
    public static final String MEM_STORE_SIZE = "memStoreSize";
    //RegionServer因为文件太多导致更新被阻塞的时间（毫秒）
    public static final String UPDATES_BLOCKED_TIME = "updatesBlockedTime";
    //命中BlockCache的次数
    public static final String BLOCK_CACHE_HIT_COUNT = "blockCacheHitCount";
    //未命中BlockCache的次数
    public static final String BLOCK_CACHE_MISS_COUNT = "blockCacheMissCount";
    //BlockCache缓存命中率
    public static final String BLOCK_CACHE_EXPRESS_HIT_PERCENT = "blockCacheExpressHitPercent";
    //RegionServer上数据本地率
    public static final String PERCENT_FILES_LOCAL = "percentFilesLocal";
    //RegionServer上slow get的数量
    public static final String SLOW_GET_COUNT = "slowGetCount";
    /**
     * HDFS监控指标
     */
    //总的文件数量
    public static final String TOTAL_FILES = "TotalFiles";
    //总的blocks数量
    public static final String TOTAL_BLOCKS = "TotalBlocks";
    //HDFS使用占比
    public static final String PERCENT_USED = "PercentUsed";
    //存活的DataNode数量
    public static final String NUM_LIVE_DATA_NODES = "NumLiveDataNodes";
    //死亡的DataNode数量
    public static final String NUM_DEAD_DATA_NODES = "NumDeadDataNodes";
    //坏盘的数量
    public static final String VOLUME_FAILURES_TOTAL = "VolumeFailuresTotal";
    //MissingBlocks
    public static final String NUMBER_OF_MISSING_BLOCKS = "NumberOfMissingBlocks";
    //RPC平均排队时间
    public static final String RPC_QUEUE_TIME_AVG_TIME = "RpcQueueTimeAvgTime";
    //PRC处理的平均时间
    public static final String RPC_PROCESSING_TIME_AVG_TIME = "RpcProcessingTimeAvgTime";
    //RPC Call队列的长度
    public static final String CALL_QUEUE_LENGTH = "CallQueueLength";
    //当前堆内存
    public static final String MEM_HEAP_USED_M = "MemHeapUsedM";
    //线程阻塞数量
    public static final String THREADS_BLOCKED = "ThreadsBlocked";
    //线程等待数量
    public static final String THREADS_WAITING = "ThreadsWaiting";
    //超过GC警告阈值的次数
    public static final String GC_NUM_WARN_THRESHOLD_EXCEEDED = "GcNumWarnThresholdExceeded";
    //读取block的平均时间
    public static final String READ_BLOCK_OP_AVG_TIME = "ReadBlockOpAvgTime";
    //写数据块的平均时间
    public static final String WRITE_BLOCK_OP_AVG_TIME = "WriteBlockOpAvgTime";
    /**
     * YARN 监控指标
     */
    //队列名称
    public static final String SUPER_QUEUE = "_SUPER";
    public static final String OTT_QUEUE = "_OTT";
    public static final String DEFAULT_QUEUE = "_DEFAULT";
    //NM存活节点数量
    public static final String NUM_ACTIVE_NMS = "NumActiveNMs";
    //|NM丢失节点数量监控)
    public static final String NUM_LOST_NMS = "NumLostNMs";
    //NM不健康节点数量监控
    public static final String NUM_UNHEALTHY_NMS = "NumUnhealthyNMs";
    //app提交数量
    public static final String APPS_SUBMITTED = "AppsSubmitted";
    //app的运行数量
    public static final String APPS_RUNNING = "AppsRunning";
    //app等待数量
    public static final String APPS_PENDING = "AppsPending";
    //app完成数量
    public static final String APPS_COMPLETED = "AppsCompleted";
    //app被kill的数量
    public static final String APPS_KILLED = "AppsKilled";
    //app失败数量
    public static final String APPS_FAILED = "AppsFailed";
    //已分配的内存大小
    public static final String ALLOCATED_MB = "AllocatedMB";
    //已分配的核数量
    public static final String ALLOCATED_V_CORES = "AllocatedVCores";
    //已分配的Container数量
    public static final String ALLOCATED_CONTAINERS = "AllocatedContainers";
    //可用的内存大小
    public static final String AVAILABLE_MB = "AvailableMB";
    //可用核数量
    public static final String AVAILABLE_V_CORES = "AvailableVCores";
    //等待分配的内存大小
    public static final String PENDING_MB = "PendingMB";
    //等待分配的核数量
    public static final String PENDING_V_CORES = "PendingVCores";
    //等待分配的Container数量
    public static final String PENDING_CONTAINERS = "PendingContainers";
    //预留的内存大小
    public static final String RESERVED_MB = "ReservedMB";
    //预留的核数量
    public static final String RESERVED_V_CORES = "ReservedVCores";
    //预留的Container数量
    public static final String RESERVED_CONTAINERS = "ReservedContainers";
    //NM心跳汇报次数
    public static final String NODE_UPDATE_CALL_NUM_OPS = "NodeUpdateCallNumOps";
    //心跳汇报处理时间
    public static final String NODE_UPDATE_CALL_AVG_TIME = "NodeUpdateCallAvgTime";
    /**
     * Zookeeper 监控指标
     */
    //zk状态（leader/follower）
    public static final String ZK_SERVER_STATE = "zk_server_state";
    //zk follower数量
    public static final String ZK_FOLLOWERS = "zk_followers";
    //zk 保持同步的follower数量
    public static final String ZK_SYNCED_FOLLOWERS = "zk_synced_followers";
    //zk 堆积请求数
    public static final String ZK_OUTSTANDING_REQUESTS = "zk_outstanding_requests";
    //zk 待同步数量
    public static final String ZK_PENDING_SYNCS = "zk_pending_syncs";
    //znode数量
    public static final String ZK_ZNODE_COUNT = "zk_znode_count";
    //数据大小
    public static final String ZK_APPROXIMATE_DATA_SIZE = "zk_approximate_data_size";
    //打开的文件描述符数量
    public static final String ZK_OPEN_FILE_DESCRIPTOR_COUNT = "zk_open_file_descriptor_count";
    //最大文件描述符数量
    public static final String ZK_MAX_FILE_DESCRIPTOR_COUNT = "zk_max_file_descriptor_count";
    //watch数量
    public static final String ZK_WATCH_COUNT = "zk_watch_count";
    //平均延时
    public static final String ZK_AVG_LATENCY = "zk_avg_latency";
    //最大延时
    public static final String ZK_MAX_LATENCY = "zk_max_latency";
    //最小延时
    public static final String ZK_MIN_LATENCY = "zk_min_latency";
    //收包数
    public static final String ZK_PACKETS_RECEIVED = "zk_packets_received";
    //发包数
    public static final String ZK_PACKETS_SENT = "zk_packets_sent";
    //连接数
    public static final String ZK_NUM_ALIVE_CONNECTIONS = "zk_num_alive_connections";


}
