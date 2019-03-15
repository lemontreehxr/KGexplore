DROP DATABASE IF EXISTS dbpedia_2014;
CREATE DATABASE dbpedia_2014;
USE dbpedia_2014;

drop table if exists triple;
create table triple
(
sourceId  int not null,
relationId  int not null,
targetId  int not null,
key sourceId2relationId_index (sourceId, relationId),
key targetId2relationId_index (targetId, relationId)
)
ENGINE=myisam default charset=utf8;
load data local infile '/home/chenjun/KGexplorer/dataset_2014/triple2id.txt' into table triple fields terminated by '\t' lines terminated by '\n' (sourceId, relationId, targetId);


drop table if exists triple_statistic;
create table triple_statistic
(
direction int not null,
relationId int not null,
entityId int not null,
size int not null,
key direction2relationId2entityId_index (direction, relationId, entityId)
)
ENGINE=myisam default charset=utf8;
load data local infile '/Users/c.j./Desktop/KGexplorer/dataset_2014/statistic.txt' into table triple_statistic fields terminated by '\t' lines terminated by '\n' (direction, relationId, entityId, size);

drop table if exists description;
create table description
(
entityId int not null,
abstract TEXT,
imageUrl VARCHAR(255),
unique key id_hash (entityId) using hash
)
ENGINE=myisam default charset=utf8;
load data local infile '/home/chenjun/KGexplorer/dataset_2014/entity2description.txt' into table description character set utf8mb4 fields terminated by '\t' lines terminated by '\n' (entityId, abstract, imageUrl);

load data local infile '/home/chenjun/KGexplorer/dataset_2014/entity2description.txt' into table description character set utf8mb4 fields terminated by '\t' lines terminated by '\n' (entityId, abstract, imageUrl);


DROP DATABASE IF EXISTS dbpedia_test;
CREATE DATABASE dbpedia_test;
USE dbpedia_test;

drop table if exists triple;
create table triple
(
sourceId  int not null,
relationId  int not null,
targetId  int not null,
key sourceId2relationId_index (sourceId, relationId),
key targetId2relationId_index (targetId, relationId)
)
ENGINE=myisam default charset=utf8;

load data local infile '/Users/c.j./Desktop/KGexplorer/dataset_test/sanyuanzu.txt' into table triple fields terminated by '\t' lines terminated by '\n' (sourceId, relationId, targetId);


drop table if exists triple_statistic;
create table triple_statistic
(
direction int not null,
relationId int not null,
entityId int not null,
size int not null,
key direction2relationId2entityId_index (direction, relationId, entityId)
)
ENGINE=myisam default charset=utf8;
load data local infile '/Users/c.j./Desktop/KGexplorer/dataset_test/statistic.txt' into table triple_statistic fields terminated by '\t' lines terminated by '\n' (direction, relationId, entityId, size);

