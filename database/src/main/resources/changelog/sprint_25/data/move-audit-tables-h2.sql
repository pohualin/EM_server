CREATE TABLE audit.client_audit AS SELECT *
                                   FROM client_audit;
CREATE TABLE audit.client_group_audit AS SELECT *
                                         FROM client_group_audit;
CREATE TABLE audit.client_group_tag_audit AS SELECT *
                                             FROM client_group_tag_audit;
CREATE TABLE audit.client_location_audit AS SELECT *
                                            FROM client_location_audit;
CREATE TABLE audit.client_note_audit AS SELECT *
                                        FROM client_note_audit;
CREATE TABLE audit.client_password_configuration_audit AS SELECT *
                                                          FROM client_password_configuration_audit;
CREATE TABLE audit.client_patient_audit AS SELECT *
                                           FROM client_patient_audit;
CREATE TABLE audit.client_provider_audit AS SELECT *
                                            FROM client_provider_audit;
CREATE TABLE audit.client_region_audit AS SELECT *
                                          FROM client_region_audit;
CREATE TABLE audit.client_restrict_configuration_audit AS SELECT *
                                                          FROM client_restrict_configuration_audit;
CREATE TABLE audit.client_team_audit AS SELECT *
                                        FROM client_team_audit;
CREATE TABLE audit.client_team_email_configuration_audit AS SELECT *
                                                            FROM client_team_email_configuration_audit;
CREATE TABLE audit.client_team_location_audit AS SELECT *
                                                 FROM client_team_location_audit;
CREATE TABLE audit.client_team_phone_configuration_audit AS SELECT *
                                                            FROM client_team_phone_configuration_audit;
CREATE TABLE audit.client_team_scheduling_configuration_audit AS SELECT *
                                                                 FROM client_team_scheduling_configuration_audit;
CREATE TABLE audit.client_team_self_reg_config_audit AS SELECT *
                                                        FROM client_team_self_reg_config_audit;
CREATE TABLE audit.client_team_tag_audit AS SELECT *
                                            FROM client_team_tag_audit;
CREATE TABLE audit.client_tier_audit AS SELECT *
                                        FROM client_tier_audit;
CREATE TABLE audit.client_type_audit AS SELECT *
                                        FROM client_type_audit;
CREATE TABLE audit.default_client_team_scheduling_configuration_audit AS SELECT *
                                                                         FROM
                                                                           default_client_team_scheduling_configuration_audit;
CREATE TABLE audit.default_password_configuration_audit AS SELECT *
                                                           FROM default_password_configuration_audit;
CREATE TABLE audit.email_restrict_configuration_audit AS SELECT *
                                                         FROM email_restrict_configuration_audit;
CREATE TABLE audit.info_header_config_audit AS SELECT *
                                               FROM info_header_config_audit;
CREATE TABLE audit.ip_restrict_configuration_audit AS SELECT *
                                                      FROM ip_restrict_configuration_audit;
CREATE TABLE audit.language_audit AS SELECT *
                                     FROM language_audit;
CREATE TABLE audit.location_audit AS SELECT *
                                     FROM location_audit;
CREATE TABLE audit.patient_id_label_config_audit AS SELECT *
                                                    FROM patient_id_label_config_audit;
CREATE TABLE audit.patient_id_label_type_audit AS SELECT *
                                                  FROM patient_id_label_type_audit;
CREATE TABLE audit.patient_opt_out_preference_audit AS SELECT *
                                                       FROM patient_opt_out_preference_audit;
CREATE TABLE audit.patient_self_reg_config_audit AS SELECT *
                                                    FROM patient_self_reg_config_audit;
CREATE TABLE audit.provider_audit AS SELECT *
                                     FROM provider_audit;
CREATE TABLE audit.provider_specialty_audit AS SELECT *
                                               FROM provider_specialty_audit;
CREATE TABLE audit.reference_group_audit AS SELECT *
                                            FROM reference_group_audit;
CREATE TABLE audit.reference_group_type_audit AS SELECT *
                                                 FROM reference_group_type_audit;
CREATE TABLE audit.reference_tag_audit AS SELECT *
                                          FROM reference_tag_audit;
CREATE TABLE audit.referencegroup_referencetag_audit AS SELECT *
                                                        FROM referencegroup_referencetag_audit;
CREATE TABLE audit.salesforce_client_audit AS SELECT *
                                              FROM salesforce_client_audit;
CREATE TABLE audit.salesforce_team_audit AS SELECT *
                                            FROM salesforce_team_audit;
CREATE TABLE audit.scheduled_program_audit AS SELECT *
                                              FROM scheduled_program_audit;
CREATE TABLE audit.strings_audit AS SELECT *
                                    FROM strings_audit;
CREATE TABLE audit.team_provider_audit AS SELECT *
                                          FROM team_provider_audit;
CREATE TABLE audit.team_provider_team_location_audit AS SELECT *
                                                        FROM team_provider_team_location_audit;
CREATE TABLE audit.user_admin_audit AS SELECT *
                                       FROM user_admin_audit;
CREATE TABLE audit.user_admin_role_audit AS SELECT *
                                            FROM user_admin_role_audit;
CREATE TABLE audit.user_admin_role_user_admin_permission_audit AS SELECT *
                                                                  FROM user_admin_role_user_admin_permission_audit;
CREATE TABLE audit.user_admin_user_admin_role_audit AS SELECT *
                                                       FROM user_admin_user_admin_role_audit;
CREATE TABLE audit.user_client_audit AS SELECT *
                                        FROM user_client_audit;
CREATE TABLE audit.user_client_password_history_audit AS SELECT *
                                                         FROM user_client_password_history_audit;
CREATE TABLE audit.user_client_reference_role_audit AS SELECT *
                                                       FROM user_client_reference_role_audit;
CREATE TABLE audit.user_client_reference_role_permission_audit AS SELECT *
                                                                  FROM user_client_reference_role_permission_audit;
CREATE TABLE audit.user_client_reference_role_type_audit AS SELECT *
                                                            FROM user_client_reference_role_type_audit;
CREATE TABLE audit.user_client_reference_team_role_audit AS SELECT *
                                                            FROM user_client_reference_team_role_audit;
CREATE TABLE audit.user_client_reference_team_role_permission_audit AS SELECT *
                                                                       FROM
                                                                         user_client_reference_team_role_permission_audit;
CREATE TABLE audit.user_client_reference_team_role_type_audit AS SELECT *
                                                                 FROM user_client_reference_team_role_type_audit;
CREATE TABLE audit.user_client_role_audit AS SELECT *
                                             FROM user_client_role_audit;
CREATE TABLE audit.user_client_role_user_client_permission_audit AS SELECT *
                                                                    FROM user_client_role_user_client_permission_audit;
CREATE TABLE audit.user_client_secret_question_response_audit AS SELECT *
                                                                 FROM user_client_secret_question_response_audit;
CREATE TABLE audit.user_client_team_role_audit AS SELECT *
                                                  FROM user_client_team_role_audit;
CREATE TABLE audit.user_client_team_role_user_client_team_permission_audit AS SELECT *
                                                                              FROM
                                                                                user_client_team_role_user_client_team_permission_audit;
CREATE TABLE audit.user_client_user_client_role_audit AS SELECT *
                                                         FROM user_client_user_client_role_audit;
CREATE TABLE audit.user_client_user_client_team_role_audit AS SELECT *
                                                              FROM user_client_user_client_team_role_audit;
CREATE TABLE audit.users_audit AS SELECT *
                                  FROM users_audit;

ALTER TABLE audit.client_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.client_group_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.client_group_tag_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.client_location_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.client_note_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.client_password_configuration_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.client_patient_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.client_provider_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.client_region_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.client_restrict_configuration_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.client_team_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.client_team_email_configuration_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.client_team_location_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.client_team_phone_configuration_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.client_team_scheduling_configuration_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.client_team_self_reg_config_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.client_team_tag_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.client_tier_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.client_type_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.default_client_team_scheduling_configuration_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.default_password_configuration_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.email_restrict_configuration_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.info_header_config_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.ip_restrict_configuration_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.language_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.location_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.patient_id_label_config_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.patient_id_label_type_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.patient_opt_out_preference_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.patient_self_reg_config_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.provider_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.provider_specialty_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.reference_group_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.reference_group_type_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.reference_tag_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.referencegroup_referencetag_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.salesforce_client_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.salesforce_team_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.scheduled_program_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.strings_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.team_provider_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.team_provider_team_location_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.user_admin_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.user_admin_role_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.user_admin_role_user_admin_permission_audit ALTER COLUMN role_id BIGINT NOT NULL;
ALTER TABLE audit.user_admin_role_user_admin_permission_audit ALTER COLUMN permission_name VARCHAR(100) NOT NULL;
ALTER TABLE audit.user_admin_user_admin_role_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.user_client_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.user_client_password_history_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.user_client_reference_role_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.user_client_reference_role_permission_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.user_client_reference_role_type_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.user_client_reference_team_role_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.user_client_reference_team_role_permission_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.user_client_reference_team_role_type_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.user_client_role_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.user_client_role_user_client_permission_audit ALTER COLUMN role_id BIGINT NOT NULL;
ALTER TABLE audit.user_client_role_user_client_permission_audit ALTER COLUMN permission_name VARCHAR(100) NOT NULL;
ALTER TABLE audit.user_client_secret_question_response_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.user_client_team_role_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.user_client_team_role_user_client_team_permission_audit ALTER COLUMN role_id BIGINT NOT NULL;
ALTER TABLE audit.user_client_team_role_user_client_team_permission_audit ALTER COLUMN permission_name VARCHAR(100) NOT NULL;
ALTER TABLE audit.user_client_user_client_role_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.user_client_user_client_team_role_audit ALTER COLUMN id BIGINT NOT NULL;
ALTER TABLE audit.users_audit ALTER COLUMN id BIGINT NOT NULL;


ALTER TABLE audit.client_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.client_group_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.client_group_tag_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.client_location_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.client_note_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.client_password_configuration_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.client_patient_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.client_provider_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.client_region_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.client_restrict_configuration_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.client_team_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.client_team_email_configuration_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.client_team_location_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.client_team_phone_configuration_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.client_team_scheduling_configuration_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.client_team_self_reg_config_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.client_team_tag_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.client_tier_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.client_type_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.default_client_team_scheduling_configuration_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.default_password_configuration_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.email_restrict_configuration_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.info_header_config_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.ip_restrict_configuration_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.language_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.location_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.patient_id_label_config_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.patient_id_label_type_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.patient_opt_out_preference_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.patient_self_reg_config_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.provider_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.provider_specialty_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.reference_group_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.reference_group_type_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.reference_tag_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.referencegroup_referencetag_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.salesforce_client_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.salesforce_team_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.scheduled_program_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.strings_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.team_provider_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.team_provider_team_location_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.user_admin_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.user_admin_role_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.user_admin_role_user_admin_permission_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.user_admin_user_admin_role_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.user_client_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.user_client_password_history_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.user_client_reference_role_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.user_client_reference_role_permission_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.user_client_reference_role_type_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.user_client_reference_team_role_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.user_client_reference_team_role_permission_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.user_client_reference_team_role_type_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.user_client_role_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.user_client_role_user_client_permission_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.user_client_secret_question_response_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.user_client_team_role_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.user_client_team_role_user_client_team_permission_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.user_client_user_client_role_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.user_client_user_client_team_role_audit ALTER COLUMN revision BIGINT NOT NULL;
ALTER TABLE audit.users_audit ALTER COLUMN revision BIGINT NOT NULL;


ALTER TABLE audit.client_audit ADD CONSTRAINT pk_client_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.client_group_audit ADD CONSTRAINT pk_client_group_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.client_group_tag_audit ADD CONSTRAINT pk_client_group_tag_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.client_location_audit ADD CONSTRAINT pk_client_location_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.client_note_audit ADD CONSTRAINT pk_client_note_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.client_password_configuration_audit ADD CONSTRAINT pk_client_password_configuration_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.client_patient_audit ADD CONSTRAINT pk_client_patient_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.client_provider_audit ADD CONSTRAINT pk_client_provider_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.client_region_audit ADD CONSTRAINT pk_client_region_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.client_restrict_configuration_audit ADD CONSTRAINT pk_client_restrict_configuration_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.client_team_audit ADD CONSTRAINT pk_client_team_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.client_team_email_configuration_audit ADD CONSTRAINT pk_client_team_email_configuration_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.client_team_location_audit ADD CONSTRAINT pk_client_team_location_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.client_team_phone_configuration_audit ADD CONSTRAINT pk_client_team_phone_configuration_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.client_team_scheduling_configuration_audit ADD CONSTRAINT pk_client_team_scheduling_configuration_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.client_team_self_reg_config_audit ADD CONSTRAINT pk_client_team_self_reg_config_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.client_team_tag_audit ADD CONSTRAINT pk_client_team_tag_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.client_tier_audit ADD CONSTRAINT pk_client_tier_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.client_type_audit ADD CONSTRAINT pk_client_type_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.default_client_team_scheduling_configuration_audit ADD CONSTRAINT pk_default_client_team_scheduling_configuration_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.default_password_configuration_audit ADD CONSTRAINT pk_default_password_configuration_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.email_restrict_configuration_audit ADD CONSTRAINT pk_email_restrict_configuration_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.info_header_config_audit ADD CONSTRAINT pk_info_header_config_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.ip_restrict_configuration_audit ADD CONSTRAINT pk_ip_restrict_configuration_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.language_audit ADD CONSTRAINT pk_language_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.location_audit ADD CONSTRAINT pk_location_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.patient_id_label_config_audit ADD CONSTRAINT pk_patient_id_label_config_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.patient_id_label_type_audit ADD CONSTRAINT pk_patient_id_label_type_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.patient_opt_out_preference_audit ADD CONSTRAINT pk_patient_opt_out_preference_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.patient_self_reg_config_audit ADD CONSTRAINT pk_patient_self_reg_config_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.provider_audit ADD CONSTRAINT pk_provider_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.provider_specialty_audit ADD CONSTRAINT pk_provider_specialty_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.reference_group_audit ADD CONSTRAINT pk_reference_group_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.reference_group_type_audit ADD CONSTRAINT pk_reference_group_type_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.reference_tag_audit ADD CONSTRAINT pk_reference_tag_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.referencegroup_referencetag_audit ADD CONSTRAINT pk_referencegroup_referencetag_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.salesforce_client_audit ADD CONSTRAINT pk_salesforce_client_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.salesforce_team_audit ADD CONSTRAINT pk_salesforce_team_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.scheduled_program_audit ADD CONSTRAINT pk_scheduled_program_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.strings_audit ADD CONSTRAINT pk_strings_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.team_provider_audit ADD CONSTRAINT pk_team_provider_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.team_provider_team_location_audit ADD CONSTRAINT pk_team_provider_team_location_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.user_admin_audit ADD CONSTRAINT pk_user_admin_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.user_admin_role_audit ADD CONSTRAINT pk_user_admin_role_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.user_admin_role_user_admin_permission_audit ADD CONSTRAINT pk_user_admin_role_user_admin_permission_audit PRIMARY KEY (revision, role_id, permission_name);
ALTER TABLE audit.user_admin_user_admin_role_audit ADD CONSTRAINT pk_user_admin_user_admin_role_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.user_client_audit ADD CONSTRAINT pk_user_client_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.user_client_password_history_audit ADD CONSTRAINT pk_user_client_password_history_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.user_client_reference_role_audit ADD CONSTRAINT pk_user_client_reference_role_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.user_client_reference_role_permission_audit ADD CONSTRAINT pk_user_client_reference_role_permission_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.user_client_reference_role_type_audit ADD CONSTRAINT pk_user_client_reference_role_type_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.user_client_reference_team_role_audit ADD CONSTRAINT pk_user_client_reference_team_role_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.user_client_reference_team_role_permission_audit ADD CONSTRAINT pk_user_client_reference_team_role_permission_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.user_client_reference_team_role_type_audit ADD CONSTRAINT pk_user_client_reference_team_role_type_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.user_client_role_audit ADD CONSTRAINT pk_user_client_role_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.user_client_role_user_client_permission_audit ADD CONSTRAINT pk_user_client_role_user_client_permission_audit PRIMARY KEY (revision, role_id, permission_name);
ALTER TABLE audit.user_client_secret_question_response_audit ADD CONSTRAINT pk_user_client_secret_question_response_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.user_client_team_role_audit ADD CONSTRAINT pk_user_client_team_role_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.user_client_team_role_user_client_team_permission_audit ADD CONSTRAINT pk_user_client_team_role_user_client_team_permission_audit PRIMARY KEY (revision, role_id, permission_name);
ALTER TABLE audit.user_client_user_client_role_audit ADD CONSTRAINT pk_user_client_user_client_role_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.user_client_user_client_team_role_audit ADD CONSTRAINT pk_user_client_user_client_team_role_audit PRIMARY KEY (id, revision);
ALTER TABLE audit.users_audit ADD CONSTRAINT pk_users_audit PRIMARY KEY (id, revision);

ALTER TABLE audit.client_audit ADD CONSTRAINT fk_client_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.client_group_audit ADD CONSTRAINT fk_client_group_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.client_group_tag_audit ADD CONSTRAINT fk_client_group_tag_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.client_location_audit ADD CONSTRAINT fk_client_location_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.client_note_audit ADD CONSTRAINT fk_client_note_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.client_password_configuration_audit ADD CONSTRAINT fk_client_password_configuration_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.client_patient_audit ADD CONSTRAINT fk_client_patient_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.client_provider_audit ADD CONSTRAINT fk_client_provider_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.client_region_audit ADD CONSTRAINT fk_client_region_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.client_restrict_configuration_audit ADD CONSTRAINT fk_client_restrict_configuration_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.client_team_audit ADD CONSTRAINT fk_client_team_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.client_team_email_configuration_audit ADD CONSTRAINT fk_client_team_email_configuration_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.client_team_location_audit ADD CONSTRAINT fk_client_team_location_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.client_team_phone_configuration_audit ADD CONSTRAINT fk_client_team_phone_configuration_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.client_team_scheduling_configuration_audit ADD CONSTRAINT fk_client_team_scheduling_configuration_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.client_team_self_reg_config_audit ADD CONSTRAINT fk_client_team_self_reg_config_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.client_team_tag_audit ADD CONSTRAINT fk_client_team_tag_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.client_tier_audit ADD CONSTRAINT fk_client_tier_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.client_type_audit ADD CONSTRAINT fk_client_type_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.default_client_team_scheduling_configuration_audit ADD CONSTRAINT fk_default_client_team_scheduling_configuration_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.default_password_configuration_audit ADD CONSTRAINT fk_default_password_configuration_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.email_restrict_configuration_audit ADD CONSTRAINT fk_email_restrict_configuration_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.info_header_config_audit ADD CONSTRAINT fk_info_header_config_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.ip_restrict_configuration_audit ADD CONSTRAINT fk_ip_restrict_configuration_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.language_audit ADD CONSTRAINT fk_language_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.location_audit ADD CONSTRAINT fk_location_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.patient_id_label_config_audit ADD CONSTRAINT fk_patient_id_label_config_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.patient_id_label_type_audit ADD CONSTRAINT fk_patient_id_label_type_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.patient_opt_out_preference_audit ADD CONSTRAINT fk_patient_opt_out_preference_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.patient_self_reg_config_audit ADD CONSTRAINT fk_patient_self_reg_config_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.provider_audit ADD CONSTRAINT fk_provider_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.provider_specialty_audit ADD CONSTRAINT fk_provider_specialty_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.reference_group_audit ADD CONSTRAINT fk_reference_group_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.reference_group_type_audit ADD CONSTRAINT fk_reference_group_type_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.reference_tag_audit ADD CONSTRAINT fk_reference_tag_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.referencegroup_referencetag_audit ADD CONSTRAINT fk_referencegroup_referencetag_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.salesforce_client_audit ADD CONSTRAINT fk_salesforce_client_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.salesforce_team_audit ADD CONSTRAINT fk_salesforce_team_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.scheduled_program_audit ADD CONSTRAINT fk_scheduled_program_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.strings_audit ADD CONSTRAINT fk_strings_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.team_provider_audit ADD CONSTRAINT fk_team_provider_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.team_provider_team_location_audit ADD CONSTRAINT fk_team_provider_team_location_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.user_admin_audit ADD CONSTRAINT fk_user_admin_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.user_admin_role_audit ADD CONSTRAINT fk_user_admin_role_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.user_admin_role_user_admin_permission_audit ADD CONSTRAINT fk_user_admin_role_user_admin_permission_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.user_admin_user_admin_role_audit ADD CONSTRAINT fk_user_admin_user_admin_role_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.user_client_audit ADD CONSTRAINT fk_user_client_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.user_client_password_history_audit ADD CONSTRAINT fk_user_client_password_history_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.user_client_reference_role_audit ADD CONSTRAINT fk_user_client_reference_role_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.user_client_reference_role_permission_audit ADD CONSTRAINT fk_user_client_reference_role_permission_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.user_client_reference_role_type_audit ADD CONSTRAINT fk_user_client_reference_role_type_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.user_client_reference_team_role_audit ADD CONSTRAINT fk_user_client_reference_team_role_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.user_client_reference_team_role_permission_audit ADD CONSTRAINT fk_user_client_reference_team_role_permission_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.user_client_reference_team_role_type_audit ADD CONSTRAINT fk_user_client_reference_team_role_type_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.user_client_role_audit ADD CONSTRAINT fk_user_client_role_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.user_client_role_user_client_permission_audit ADD CONSTRAINT fk_user_client_role_user_client_permission_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.user_client_secret_question_response_audit ADD CONSTRAINT fk_user_client_secret_question_response_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.user_client_team_role_audit ADD CONSTRAINT fk_user_client_team_role_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.user_client_team_role_user_client_team_permission_audit ADD CONSTRAINT fk_user_client_team_role_user_client_team_permission_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.user_client_user_client_role_audit ADD CONSTRAINT fk_user_client_user_client_role_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.user_client_user_client_team_role_audit ADD CONSTRAINT fk_user_client_user_client_team_role_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);
ALTER TABLE audit.users_audit ADD CONSTRAINT fk_users_audit_revision_info FOREIGN KEY (revision) REFERENCES audit.revision_info (revision);


DROP TABLE public.client_audit;
DROP TABLE public.client_group_audit;
DROP TABLE public.client_group_tag_audit;
DROP TABLE public.client_location_audit;
DROP TABLE public.client_note_audit;
DROP TABLE public.client_password_configuration_audit;
DROP TABLE public.client_patient_audit;
DROP TABLE public.client_provider_audit;
DROP TABLE public.client_region_audit;
DROP TABLE public.client_restrict_configuration_audit;
DROP TABLE public.client_team_audit;
DROP TABLE public.client_team_email_configuration_audit;
DROP TABLE public.client_team_location_audit;
DROP TABLE public.client_team_phone_configuration_audit;
DROP TABLE public.client_team_scheduling_configuration_audit;
DROP TABLE public.client_team_self_reg_config_audit;
DROP TABLE public.client_team_tag_audit;
DROP TABLE public.client_tier_audit;
DROP TABLE public.client_type_audit;
DROP TABLE public.default_client_team_scheduling_configuration_audit;
DROP TABLE public.default_password_configuration_audit;
DROP TABLE public.email_restrict_configuration_audit;
DROP TABLE public.info_header_config_audit;
DROP TABLE public.ip_restrict_configuration_audit;
DROP TABLE public.language_audit;
DROP TABLE public.location_audit;
DROP TABLE public.patient_id_label_config_audit;
DROP TABLE public.patient_id_label_type_audit;
DROP TABLE public.patient_opt_out_preference_audit;
DROP TABLE public.patient_self_reg_config_audit;
DROP TABLE public.provider_audit;
DROP TABLE public.provider_specialty_audit;
DROP TABLE public.reference_group_audit;
DROP TABLE public.reference_group_type_audit;
DROP TABLE public.reference_tag_audit;
DROP TABLE public.referencegroup_referencetag_audit;
DROP TABLE public.salesforce_client_audit;
DROP TABLE public.salesforce_team_audit;
DROP TABLE public.scheduled_program_audit;
DROP TABLE public.strings_audit;
DROP TABLE public.team_provider_audit;
DROP TABLE public.team_provider_team_location_audit;
DROP TABLE public.user_admin_audit;
DROP TABLE public.user_admin_role_audit;
DROP TABLE public.user_admin_role_user_admin_permission_audit;
DROP TABLE public.user_admin_user_admin_role_audit;
DROP TABLE public.user_client_audit;
DROP TABLE public.user_client_password_history_audit;
DROP TABLE public.user_client_reference_role_audit;
DROP TABLE public.user_client_reference_role_permission_audit;
DROP TABLE public.user_client_reference_role_type_audit;
DROP TABLE public.user_client_reference_team_role_audit;
DROP TABLE public.user_client_reference_team_role_permission_audit;
DROP TABLE public.user_client_reference_team_role_type_audit;
DROP TABLE public.user_client_role_audit;
DROP TABLE public.user_client_role_user_client_permission_audit;
DROP TABLE public.user_client_secret_question_response_audit;
DROP TABLE public.user_client_team_role_audit;
DROP TABLE public.user_client_team_role_user_client_team_permission_audit;
DROP TABLE public.user_client_user_client_role_audit;
DROP TABLE public.user_client_user_client_team_role_audit;
DROP TABLE public.users_audit;
DROP TABLE public.revision_info;
