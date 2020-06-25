CREATE TABLE `enderchest_user` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `uuid` BINARY(16) NOT NULL,
    `name` VARCHAR(16) CHARACTER SET ascii COLLATE ascii_general_ci NOT NULL,
    `last_seen` DATE NOT NULL DEFAULT CURRENT_DATE
    PRIMARY KEY (`id`),
    UNIQUE KEY (`uuid`),
    INDEX (`name`)
) ENGINE InnoDB DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATION utf8mb4_unicode_520_ci;

CREATE TABLE `enderchest_inventory` (
    `user_id` BIGINT UNSIGNED NOT NULL,
    `slot` MEDIUMINT UNSIGNED NOT NULL,
    `content` MEDIUMBLOB NOT NULL,
    PRIMARY KEY (`user_id`, `slot`),
    FOREIGN KEY (`user_id`) REFERENCES `enderchest_user` (`id`) ON DELETE CASCADE
) ENGINE InnoDB DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATION utf8mb4_unicode_520_ci;
