DROP TABLE IF EXISTS `persistent_logins`;
CREATE TABLE `persistent_logins` (
  `username` varchar(64) not null,
  `series` varchar(64) not null,
  `token` varchar(64) not null,
  `last_used` timestamp not null,
  PRIMARY KEY (`series`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
