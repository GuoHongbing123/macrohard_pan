CREATE TABLE `pan_recycle_bin`  (
  `id` bigint NOT NULL COMMENT 'id',
  `fid` bigint NOT NULL COMMENT '文件/文件夹id',
  `type` int NOT NULL COMMENT '类型：文件|文件夹',
  `real_name` varchar(255) NOT NULL COMMENT '文件夹/文件',
  `full_path` varchar(255) NOT NULL COMMENT '原文件路径',
  `size` bigint NOT NULL COMMENT '文件大小|B',
  `owner_id` bigint NOT NULL COMMENT '所属用户',
  `owner_name` varchar(255) NOT NULL COMMENT '所属用户名',
  `expire_date` datetime NOT NULL COMMENT '过期时间',
  `status` int NOT NULL COMMENT '状态',
  `remark` varchar(255) NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='回收站';

CREATE TABLE `pan_file`  (
  `id` bigint NOT NULL COMMENT 'id',
  `foreign_id` bigint NULL COMMENT '外部存储id',
  `folder_id` bigint NOT NULL COMMENT '文件夹id',
  `real_name` varchar(255) NOT NULL COMMENT '文件名称',
  `full_path` varchar(255) NOT NULL COMMENT '全路径',
  `real_path` varchar(255) NOT NULL COMMENT '真实路径',
  `size` bigint NOT NULL COMMENT '文件大小|B',
  `suffix` varchar(255) NOT NULL COMMENT '文件后缀',
  `sort` varchar(255) NOT NULL DEFAULT 0 COMMENT '排序',
  `shard_index` int NOT NULL COMMENT '已上传分片',
  `shard_size` bigint NOT NULL COMMENT '分片大小|B',
  `shard_total` int NOT NULL COMMENT '分片总数',
  `thumb` varchar(255) NULL COMMENT '视频封面',
  `file_key` varchar(255) NOT NULL COMMENT '文件标识',
  `owner_id` bigint NOT NULL COMMENT '所属用户',
  `owner_name` varchar(255) NOT NULL COMMENT '所属用户名',
  `owner_type` varchar(255) NULL COMMENT '所属用户类型（冗余字段）',
  `share` int NOT NULL DEFAULT 0 COMMENT '是否分享',
  `type` varchar(255) NULL COMMENT '冗余字段',
  `status` int NOT NULL COMMENT '状态：1正常 2隐藏 3历史版本 4封禁 5禁止下载 6损坏',
  `remark` varchar(255) NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件';

ALTER TABLE `pan_file` ADD `share_id` bigint comment '分享id' AFTER `share`;
ALTER TABLE pan_file ALTER COLUMN `type` SET DEFAULT '1';

CREATE TABLE `pan_share`  (
  `id` bigint NOT NULL COMMENT 'id',
  `fid` bigint NOT NULL COMMENT '文件夹/文件id',
  `type` int NOT NULL COMMENT '类型：文件|文件夹',
  `share_type` varchar(255) NULL COMMENT '分享类型：私密|公开',
  `share_path` varchar(255) NOT NULL COMMENT '分享路径',
  `real_name` varchar(255) NOT NULL COMMENT '文件夹/文件名称',
  `code` varchar(255) NOT NULL COMMENT '提取码',
  `owner_id` bigint NOT NULL COMMENT '所属用户/分享人id',
  `owner_name` varchar(255) NOT NULL COMMENT '所属用户/分享人',
  `expire_type` int NOT NULL COMMENT '过期类型',
  `expire_date` datetime NULL COMMENT '过期时间',
  `download_num` int ZEROFILL NOT NULL COMMENT '下载次数',
  `download_limit` int NULL COMMENT '限制下载次数',
  `status` int NOT NULL COMMENT '状态：0永久 1正常 2过期 3次数用尽 4违规',
  `remark` varchar(255) NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分享信息';

CREATE TABLE `pan_folder`  (
  `id` bigint NOT NULL COMMENT 'id',
  `pid` bigint NOT NULL COMMENT '父文件夹id',
  `real_name` varchar(255) NOT NULL COMMENT '文件夹名称（默认根目录\"/\"）',
  `full_path` varchar(255) NOT NULL COMMENT '文件夹全路径（默认根目录\"/\"）',
  `sort` varchar(255) NOT NULL DEFAULT 0 COMMENT '排序',
  `empty` int NOT NULL DEFAULT 0 COMMENT '是否为空',
  `owner_id` bigint NOT NULL COMMENT '所属用户',
  `owner_name` varchar(255) NOT NULL COMMENT '所属用户名',
  `owner_type` varchar(255) NULL COMMENT '所属用户类型（冗余字段）',
  `share` int NOT NULL DEFAULT 0 COMMENT '是否分享',
  `share_id` bigint NULL COMMENT '分享id',
  `type` varchar(255) NULL COMMENT '冗余字段',
  `status` int NOT NULL COMMENT '状态：1正常 2隐藏 4封禁',
  `remark` varchar(255) NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件夹';

ALTER TABLE pan_folder ALTER COLUMN `type` SET DEFAULT '2';
ALTER TABLE pan_folder ALTER COLUMN is_empty SET DEFAULT '0';

CREATE TABLE `pan_user`  (
  `id` bigint NOT NULL,
  `sys_id` bigint NULL COMMENT '系统用户id',
  `username` varchar(255) NOT NULL COMMENT '用户名（唯一键）',
  `nickname` varchar(255) NULL COMMENT '昵称',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `avatar` varchar(255) NULL COMMENT '头像',
  `size_limit` bigint NOT NULL DEFAULT 5368709120 COMMENT '空间限制|B',
  `total_size` bigint NOT NULL DEFAULT 0 COMMENT '已使用空间|B',
  `last_login_ip` varchar(255) NULL COMMENT '最后登录IP',
  `last_login_time` datetime NULL COMMENT '最后登录时间',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态：1超级管理员 2管理员 3注册用户 4封禁用户',
  `remark` varchar(255) NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY(`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网盘用户';

RENAME TABLE `pan_user` TO `pan_userinfo`;
ALTER TABLE `pan_userinfo` ADD `mail` varchar(255) comment '邮箱' AFTER `password`;
ALTER TABLE `pan_userinfo` ADD `mobile` varchar(64) comment '手机号' AFTER `password`;

CREATE TABLE `pan_download_token` (
    `id` bigint NOT NULL,
    `file_key` varchar(255) NOT NULL COMMENT '标识',
    `real_name` varchar(255) NOT NULL COMMENT '文件名称',
    `full_path` varchar(255) NOT NULL COMMENT '真实路径',
    `token` varchar(255) NOT NULL COMMENT 'token',
    `expire_date` datetime NOT NULL COMMENT '过期时间',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE (`token`),
    UNIQUE (`file_key`)
)ENGING=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='下载TOKEN';