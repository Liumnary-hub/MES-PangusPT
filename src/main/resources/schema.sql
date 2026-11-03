CREATE TABLE IF NOT EXISTS `mes_bom` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `bom_code` VARCHAR(500) NULL,
  `product_id` BIGINT NULL,
  `product_code` VARCHAR(500) NULL,
  `version` VARCHAR(500) NULL,
  `status` VARCHAR(500) NULL,
  `effective_date` DATETIME NULL,
  `expiry_date` DATETIME NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `create_user` VARCHAR(500) NULL,
  `update_user` VARCHAR(500) NULL,
  `is_deleted` INT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `mes_bom_detail` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `bom_id` BIGINT NULL,
  `material_id` BIGINT NULL,
  `material_code` VARCHAR(500) NULL,
  `material_name` VARCHAR(500) NULL,
  `quantity` DECIMAL(18,4) NULL,
  `unit` VARCHAR(500) NULL,
  `loss_rate` DECIMAL(18,4) NULL,
  `sequence` INT NULL,
  `process_step` VARCHAR(500) NULL,
  `is_key_material` TINYINT(1) NULL,
  `remark` VARCHAR(500) NULL,
  `create_time` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `mes_defect_code` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `defect_code` VARCHAR(500) NULL,
  `defect_name` VARCHAR(500) NULL,
  `defect_type` VARCHAR(500) NULL,
  `severity` VARCHAR(500) NULL,
  `description` VARCHAR(500) NULL,
  `handling_method` VARCHAR(500) NULL,
  `preventive_action` VARCHAR(500) NULL,
  `responsible_dept` VARCHAR(500) NULL,
  `enabled` TINYINT(1) NULL,
  `remark` VARCHAR(500) NULL,
  `create_by` VARCHAR(500) NULL,
  `create_time` DATETIME NULL,
  `update_by` VARCHAR(500) NULL,
  `update_time` DATETIME NULL,
  `deleted` INT NULL,
  `defect_type_name` VARCHAR(500) NULL,
  `severity_name` VARCHAR(500) NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `mes_defect_detail` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `record_id` BIGINT NULL,
  `task_id` BIGINT NULL,
  `defect_code_id` BIGINT NULL,
  `defect_code` VARCHAR(500) NULL,
  `defect_name` VARCHAR(500) NULL,
  `defect_quantity` INT NULL,
  `defect_location` VARCHAR(500) NULL,
  `severity` VARCHAR(500) NULL,
  `description` VARCHAR(500) NULL,
  `cause_analysis` VARCHAR(500) NULL,
  `temporary_action` VARCHAR(500) NULL,
  `permanent_action` VARCHAR(500) NULL,
  `responsible_person` VARCHAR(500) NULL,
  `remark` VARCHAR(500) NULL,
  `images` VARCHAR(500) NULL,
  `create_by` VARCHAR(500) NULL,
  `create_time` DATETIME NULL,
  `update_by` VARCHAR(500) NULL,
  `update_time` DATETIME NULL,
  `deleted` INT NULL,
  `product_code` VARCHAR(500) NULL,
  `product_name` VARCHAR(500) NULL,
  `batch_no` VARCHAR(500) NULL,
  `process_code` VARCHAR(500) NULL,
  `process_name` VARCHAR(500) NULL,
  `defect_type` VARCHAR(500) NULL,
  `responsible_dept` VARCHAR(500) NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `mes_inventory` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `material_id` BIGINT NULL,
  `material_code` VARCHAR(500) NULL,
  `location_id` BIGINT NULL,
  `location_code` VARCHAR(500) NULL,
  `current_quantity` DECIMAL(18,4) NULL,
  `locked_quantity` DECIMAL(18,4) NULL,
  `available_quantity` DECIMAL(18,4) NULL,
  `batch_number` VARCHAR(500) NULL,
  `supplier_batch` VARCHAR(500) NULL,
  `production_date` DATE NULL,
  `expiry_date` DATE NULL,
  `quality_status` VARCHAR(500) NULL,
  `last_update_time` DATETIME NULL,
  PRIMARY KEY (`id`),
  KEY `idx_material` (`material_id`),
  KEY `idx_batch` (`batch_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `mes_inventory_transaction` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `transaction_type` VARCHAR(500) NULL,
  `material_id` BIGINT NULL,
  `material_code` VARCHAR(500) NULL,
  `from_location_id` BIGINT NULL,
  `from_location_code` VARCHAR(500) NULL,
  `to_location_id` BIGINT NULL,
  `to_location_code` VARCHAR(500) NULL,
  `quantity` DECIMAL(18,4) NULL,
  `batch_number` VARCHAR(500) NULL,
  `reference_type` VARCHAR(500) NULL,
  `reference_id` BIGINT NULL,
  `reference_code` VARCHAR(500) NULL,
  `transaction_time` DATETIME NULL,
  `operator_id` VARCHAR(500) NULL,
  `operator_name` VARCHAR(500) NULL,
  `remark` VARCHAR(500) NULL,
  `create_time` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `mes_material` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `material_code` VARCHAR(500) NULL,
  `material_name` VARCHAR(500) NULL,
  `material_type` VARCHAR(500) NULL,
  `specification` VARCHAR(500) NULL,
  `unit` VARCHAR(500) NULL,
  `safety_stock` DECIMAL(18,4) NULL,
  `max_stock` DECIMAL(18,4) NULL,
  `min_stock` DECIMAL(18,4) NULL,
  `status` VARCHAR(500) NULL,
  `supplier_code` VARCHAR(500) NULL,
  `storage_condition` VARCHAR(500) NULL,
  `shelf_life` INT NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `create_user` VARCHAR(500) NULL,
  `update_user` VARCHAR(500) NULL,
  `is_deleted` INT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `mes_pick_list` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `pick_list_code` VARCHAR(500) NULL,
  `work_order_id` BIGINT NULL,
  `work_order_code` VARCHAR(500) NULL,
  `task_id` BIGINT NULL,
  `task_code` VARCHAR(500) NULL,
  `status` VARCHAR(500) NULL,
  `picker_id` VARCHAR(500) NULL,
  `picker_name` VARCHAR(500) NULL,
  `pick_time` DATETIME NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `create_user` VARCHAR(500) NULL,
  `update_user` VARCHAR(500) NULL,
  `is_deleted` INT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `mes_pick_list_detail` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `pick_list_id` BIGINT NULL,
  `material_id` BIGINT NULL,
  `material_code` VARCHAR(500) NULL,
  `location_id` BIGINT NULL,
  `location_code` VARCHAR(500) NULL,
  `batch_number` VARCHAR(500) NULL,
  `required_quantity` DECIMAL(18,4) NULL,
  `picked_quantity` DECIMAL(18,4) NULL,
  `status` VARCHAR(500) NULL,
  `remark` VARCHAR(500) NULL,
  PRIMARY KEY (`id`),
  KEY `idx_pick_list` (`pick_list_id`),
  KEY `idx_batch` (`batch_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `mes_process_route` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `route_code` VARCHAR(500) NULL,
  `route_name` VARCHAR(500) NULL,
  `product_id` BIGINT NULL,
  `version` VARCHAR(500) NULL,
  `status` VARCHAR(500) NULL,
  `description` VARCHAR(500) NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `create_user` VARCHAR(500) NULL,
  `update_user` VARCHAR(500) NULL,
  `is_deleted` INT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `mes_process_step` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `step_code` VARCHAR(500) NULL,
  `step_name` VARCHAR(500) NULL,
  `route_id` BIGINT NULL,
  `sequence` INT NULL,
  `resource_type` VARCHAR(500) NULL,
  `standard_time` DOUBLE NULL,
  `setup_time` DOUBLE NULL,
  `description` VARCHAR(500) NULL,
  `quality_standard` VARCHAR(500) NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `create_user` VARCHAR(500) NULL,
  `update_user` VARCHAR(500) NULL,
  `is_deleted` INT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `mes_product_definition` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `product_code` VARCHAR(500) NULL,
  `product_name` VARCHAR(500) NULL,
  `product_type` VARCHAR(500) NULL,
  `specification` VARCHAR(500) NULL,
  `version` VARCHAR(500) NULL,
  `unit` VARCHAR(500) NULL,
  `status` VARCHAR(500) NULL,
  `description` VARCHAR(500) NULL,
  `technical_parameters` VARCHAR(500) NULL,
  `material` VARCHAR(500) NULL,
  `weight` DOUBLE NULL,
  `safety_stock` INT NULL,
  `process_flow` VARCHAR(500) NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `create_user` VARCHAR(500) NULL,
  `update_user` VARCHAR(500) NULL,
  `is_deleted` INT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `mes_production_data` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `task_id` BIGINT NULL,
  `data_type` VARCHAR(500) NULL,
  `data_value` VARCHAR(500) NULL,
  `collection_time` DATETIME NULL,
  `operator_id` VARCHAR(500) NULL,
  `remark` VARCHAR(500) NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `create_user` VARCHAR(500) NULL,
  `update_user` VARCHAR(500) NULL,
  `is_deleted` INT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_task_type_time` (`task_id`, `data_type`, `collection_time`),
  KEY `idx_is_deleted` (`is_deleted`),
  KEY `idx_task_time` (`task_id`, `collection_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `mes_production_order` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `order_code` VARCHAR(500) NULL,
  `product_id` BIGINT NULL,
  `product_code` VARCHAR(500) NULL,
  `planned_quantity` INT NULL,
  `completed_quantity` INT NULL,
  `planned_start_time` DATETIME NULL,
  `planned_end_time` DATETIME NULL,
  `actual_start_time` DATETIME NULL,
  `actual_end_time` DATETIME NULL,
  `priority` INT NULL,
  `status` VARCHAR(500) NULL,
  `remark` VARCHAR(500) NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `create_user` VARCHAR(500) NULL,
  `update_user` VARCHAR(500) NULL,
  `is_deleted` INT NULL,
  `scheduling_priority` INT NULL,
  `scheduling_status` VARCHAR(500) NULL,
  `estimated_duration` INT NULL,
  `due_date` DATETIME NULL,
  `customer_priority` INT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `mes_production_resource` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `resource_code` VARCHAR(500) NULL,
  `resource_name` VARCHAR(500) NULL,
  `resource_type` VARCHAR(500) NULL,
  `specification` VARCHAR(500) NULL,
  `capacity` VARCHAR(500) NULL,
  `department` VARCHAR(500) NULL,
  `status` VARCHAR(500) NULL,
  `description` VARCHAR(500) NULL,
  `technical_parameters` VARCHAR(500) NULL,
  `purchase_date` DATETIME NULL,
  `manufacturer` VARCHAR(500) NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `create_user` VARCHAR(500) NULL,
  `update_user` VARCHAR(500) NULL,
  `is_deleted` INT NULL,
  `efficiency` DOUBLE NULL,
  `availability` VARCHAR(500) NULL,
  PRIMARY KEY (`id`),
  KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `mes_production_task` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `task_code` VARCHAR(500) NULL,
  `order_id` BIGINT NULL,
  `order_code` VARCHAR(500) NULL,
  `product_id` BIGINT NULL,
  `product_code` VARCHAR(500) NULL,
  `resource_id` BIGINT NULL,
  `resource_name` VARCHAR(500) NULL,
  `operation_sequence` INT NULL,
  `operation_name` VARCHAR(500) NULL,
  `planned_quantity` INT NULL,
  `completed_quantity` INT NULL,
  `qualified_quantity` INT NULL,
  `standard_time` DOUBLE NULL,
  `actual_time` DOUBLE NULL,
  `planned_start_time` DATETIME NULL,
  `planned_end_time` DATETIME NULL,
  `actual_start_time` DATETIME NULL,
  `actual_end_time` DATETIME NULL,
  `operator_id` VARCHAR(500) NULL,
  `operator_name` VARCHAR(500) NULL,
  `status` VARCHAR(500) NULL,
  `quality_status` VARCHAR(500) NULL,
  `remark` VARCHAR(500) NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `create_user` VARCHAR(500) NULL,
  `update_user` VARCHAR(500) NULL,
  `is_deleted` INT NULL,
  `setup_time` INT NULL,
  `teardown_time` INT NULL,
  `predecessor_task_id` BIGINT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_order` (`order_id`),
  KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `mes_quality_task` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `task_no` VARCHAR(500) NULL,
  `plan_id` BIGINT NULL,
  `standard_id` BIGINT NULL,
  `product_code` VARCHAR(500) NULL,
  `product_name` VARCHAR(500) NULL,
  `batch_no` VARCHAR(500) NULL,
  `order_no` VARCHAR(500) NULL,
  `workstation_code` VARCHAR(500) NULL,
  `inspector` VARCHAR(500) NULL,
  `inspection_date` DATETIME NULL,
  `quantity_checked` INT NULL,
  `quantity_qualified` INT NULL,
  `quantity_defective` INT NULL,
  `result` VARCHAR(500) NULL,
  `status` VARCHAR(500) NULL,
  `inspection_type` VARCHAR(500) NULL,
  `remark` VARCHAR(500) NULL,
  `create_time` DATETIME NULL,
  `complete_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `create_by` VARCHAR(500) NULL,
  `update_by` VARCHAR(500) NULL,
  `urgent` TINYINT(1) NULL,
  `workshop_code` VARCHAR(500) NULL,
  `line_code` VARCHAR(500) NULL,
  `deleted` INT NULL,
  `priority` INT NULL,
  `due_date` DATETIME NULL,
  `sample_quantity` INT NULL,
  `inspection_standard` VARCHAR(500) NULL,
  `pass_rate` DOUBLE NULL,
  `progress` INT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_batch_no` (`batch_no`),
  KEY `idx_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `mes_warehouse_location` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `location_code` VARCHAR(500) NULL,
  `location_name` VARCHAR(500) NULL,
  `warehouse_code` VARCHAR(500) NULL,
  `warehouse_name` VARCHAR(500) NULL,
  `location_type` VARCHAR(500) NULL,
  `capacity` DECIMAL(18,4) NULL,
  `current_usage` DECIMAL(18,4) NULL,
  `status` VARCHAR(500) NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `create_user` VARCHAR(500) NULL,
  `update_user` VARCHAR(500) NULL,
  `is_deleted` INT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `mes_work_calendar` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `calendar_date` DATE NULL,
  `day_type` VARCHAR(500) NULL,
  `work_shift_id` BIGINT NULL,
  `capacity_factor` DECIMAL(18,4) NULL,
  `remark` VARCHAR(500) NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `is_deleted` INT NULL,
  `shift_name` VARCHAR(500) NULL,
  `start_time` VARCHAR(500) NULL,
  `end_time` VARCHAR(500) NULL,
  `is_working_day` TINYINT(1) NULL,
  PRIMARY KEY (`id`),
  KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `mes_work_shift` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `shift_code` VARCHAR(500) NULL,
  `shift_name` VARCHAR(500) NULL,
  `start_time` TIME NULL,
  `end_time` TIME NULL,
  `break_start_time` TIME NULL,
  `break_end_time` TIME NULL,
  `duration` INT NULL,
  `is_active` TINYINT(1) NULL,
  `description` VARCHAR(500) NULL,
  `create_time` DATETIME NULL,
  `update_time` DATETIME NULL,
  `is_deleted` INT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 演示数据：批次 DEMO-2026-001 的完整追溯链路
INSERT IGNORE INTO `mes_production_order`
(`id`, `order_code`, `product_code`, `planned_quantity`, `completed_quantity`, `status`, `priority`, `create_time`, `update_time`, `is_deleted`)
VALUES (9001, 'WO-DEMO-001', 'P-DEMO-001', 100, 40, 'in_progress', 5, NOW(), NOW(), 0);

INSERT IGNORE INTO `mes_production_task`
(`id`, `task_code`, `order_id`, `order_code`, `product_code`, `operation_sequence`, `operation_name`, `planned_quantity`, `completed_quantity`, `qualified_quantity`, `status`, `quality_status`, `create_time`, `update_time`, `is_deleted`)
VALUES (9001, 'TASK-DEMO-001', 9001, 'WO-DEMO-001', 'P-DEMO-001', 1, '下料', 100, 40, 38, 'in_progress', 'pending', NOW(), NOW(), 0);

INSERT IGNORE INTO `mes_quality_task`
(`id`, `task_no`, `batch_no`, `order_no`, `product_code`, `product_name`, `quantity_checked`, `quantity_qualified`, `quantity_defective`, `result`, `status`, `inspection_type`, `deleted`, `create_time`, `update_time`)
VALUES (9001, 'QT-DEMO-001', 'DEMO-2026-001', 'WO-DEMO-001', 'P-DEMO-001', '演示产品', 40, 38, 2, 'qualified', 'COMPLETED', 'PROCESS', 0, NOW(), NOW());

INSERT IGNORE INTO `mes_defect_detail`
(`id`, `task_id`, `defect_code`, `defect_name`, `defect_quantity`, `severity`, `description`, `batch_no`, `deleted`, `create_time`)
VALUES (9001, 9001, 'D001', '表面划痕', 2, 'MINOR', '演示缺陷', 'DEMO-2026-001', 0, NOW());

INSERT IGNORE INTO `mes_inventory`
(`id`, `material_id`, `material_code`, `location_id`, `location_code`, `current_quantity`, `locked_quantity`, `available_quantity`, `batch_number`, `quality_status`, `last_update_time`)
VALUES (9001, 9001, 'M-DEMO-001', 9001, 'L-DEMO-001', 500, 0, 500, 'DEMO-2026-001', 'qualified', NOW());

INSERT IGNORE INTO `mes_pick_list`
(`id`, `pick_list_code`, `work_order_id`, `work_order_code`, `task_id`, `task_code`, `status`, `create_time`, `update_time`, `is_deleted`)
VALUES (9001, 'PL-DEMO-001', 9001, 'WO-DEMO-001', 9001, 'TASK-DEMO-001', 'completed', NOW(), NOW(), 0);

INSERT IGNORE INTO `mes_pick_list_detail`
(`id`, `pick_list_id`, `material_id`, `material_code`, `location_id`, `location_code`, `batch_number`, `required_quantity`, `picked_quantity`, `status`)
VALUES (9001, 9001, 9001, 'M-DEMO-001', 9001, 'L-DEMO-001', 'DEMO-2026-001', 10, 10, 'picked');
