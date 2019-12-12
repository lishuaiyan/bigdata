package com.dxz.bigdata.jmx.constant;

/**
 * Created by yanlishuai on 2019-11-22
 */
public class ObjectConstants {
    //HBASE
    public static final String HBASE_MASTER_SERVER = "Hadoop:service=HBase,name=Master,sub=Server";
    public static final String HBASE_MASTER_ASSIGNMENT_MANGER = "Hadoop:service=HBase,name=Master,sub=AssignmentManger";
    public static final String HBASE_REGION_SERVER_SERVER = "Hadoop:service=HBase,name=RegionServer,sub=Server";
    public static final String HBASE_REGION_SERVER_REGIONS = "Hadoop:service=HBase,name=RegionServer,sub=Regions";
    public static final String HBASE_REGION_SERVER_IPC = "Hadoop:service=HBase,name=RegionServer,sub=IPC";
    public static final String HBASE_JVM_METRICS = "Hadoop:service=HBase,name=JvmMetrics";
    //HDFS
    public static final String HADOOP_NAME_NODE_INFO = "Hadoop:service=NameNode,name=NameNodeInfo";
    public static final String HADOOP_NAME_NODE_FS_NAME_SYSTEM_STATE = "Hadoop:service=NameNode,name=FSNamesystemState";
    public static final String HADOOP_NAME_NODE_JVM_METRICS = "Hadoop:service=NameNode,name=JvmMetrics";
    public static final String HADOOP_NAME_NODE_RPC_ACTIVITY_FOR_PORT_8022 = "Hadoop:service=NameNode,name=RpcActivityForPort8022";
    public static final String HADOOP_NAME_NODE_RPC_ACTIVITY_FOR_PORT_8020 = "Hadoop:service=NameNode,name=RpcActivityForPort8020";
    public static final String HADOOP_DATA_NODE_RPC_ACTIVITY_FOR_PORT_50020 = "Hadoop:service=DataNode,name=RpcActivityForPort50020";
    public static final String HADOOP_DATA_NODE_JVM_METRICS = "Hadoop:service=DataNode,name=JvmMetrics";
    //YARN
    public static final String YARN_RESOURCE_MANAGER_CLUSTER_MRTRICS = "Hadoop:service=ResourceManager,name=ClusterMetrics";
    public static final String YARN_RESOURCE_MANAGER_QUEUE_METRICS_SUPER = "Hadoop:service=ResourceManager,name=QueueMetrics,q0=root,q1=super";
    public static final String YARN_RESOURCE_MANAGER_QUEUE_METRICS_DEFAULT = "Hadoop:service=ResourceManager,name=QueueMetrics,q0=root,q1=default";
    public static final String YARN_RESOURCE_MANAGER_QUEUE_METRICS_OTT = "Hadoop:service=ResourceManager,name=QueueMetrics,q0=root,q1=ott";
    public static final String YARN_RESOURCE_MANAGER_FS_OP_DURATIONS = "Hadoop:service=ResourceManager,name=FSOpDurations";

}
