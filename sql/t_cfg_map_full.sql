-- Populating t_cfg_map
TRUNCATE TABLE t_cfg_map;
TRUNCATE TABLE t_cfg_map_monster;
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (1, '青云后山', 0, '炼气一层', '青元域', '青云山', 'bg_forest_1', '宗门后山，灵气虽薄却也有走兽化妖。乃初入道途者必经之磨练。', false, 1, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (1, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (1, 2);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (1, 1001); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (2, '落霞谷', 3, '炼气四层', '青元域', '落霞谷', 'bg_forest_1', '夕阳西下时分，谷中霞光万丈。传说有仙人在此留下机缘。', false, 2, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (2, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (2, 2);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (2, 1001); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (3, '幽冥沼泽', 6, '炼气七层', '青元域', '幽冥沼泽', 'bg_dark_1', '常年阴气笼罩，沼泽中潜伏着各种阴灵妖物。', false, 3, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (3, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (3, 2);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (3, 1001); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (4, '黑风林', 10, '筑基一层', '玄武域', '黑风林', 'bg_forest_1', '终年黑风怒号，妖物藏身暗处伺机而动。非筑基修士不敢深入其内。', false, 4, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (4, 11);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (4, 12);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (4, 1002); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (5, '灵蛇洞', 12, '筑基三层', '玄武域', '灵蛇洞', 'bg_dark_1', '盘踞着一条千年灵蛇，洞中尽是蛇妖后裔。', false, 5, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (5, 11);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (5, 12);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (5, 1002); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (6, '迷踪林', 15, '筑基六层', '玄武域', '迷踪林', 'bg_forest_1', '林中常有迷雾，进入者往往迷失方向，困死其中。', false, 6, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (6, 11);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (6, 12);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (6, 1002); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (7, '万妖山', 18, '筑基九层', '玄武域', '万妖山', 'bg_mountain_1', '妖族聚集之地，山上妖兽成群，实力参差不齐。', false, 7, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (7, 11);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (7, 12);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (7, 1002); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (8, '落日大漠', 20, '金丹一层', '朱雀域', '落日大漠', 'bg_desert_1', '千里赤地，水源罕见。沙怪与飞禽出没无常，极其考验道心。', false, 8, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (8, 21);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (8, 22);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (8, 1003); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (9, '火焰山', 23, '金丹四层', '朱雀域', '火焰山', 'bg_desert_1', '地脉火气冲天，山体常年被烈焰包裹。火系妖兽盘踞于此。', false, 9, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (9, 21);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (9, 22);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (9, 1003); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (10, '赤霞城废墟', 26, '金丹七层', '朱雀域', '赤霞城', 'bg_dark_1', '昔日修仙大城，毁于魔道入侵。如今只剩残垣断壁与游荡的怨灵。', false, 10, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (10, 21);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (10, 22);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (10, 1003); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (11, '蛮荒古林', 30, '元婴一层', '青龙域', '蛮荒古林', 'bg_forest_1', '太古时代遗留的原始森林，巨兽横行，危机四伏。', false, 11, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (11, 31);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (11, 32);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (11, 1004); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (12, '龙脉山', 33, '元婴四层', '青龙域', '龙脉山', 'bg_mountain_1', '传说有龙脉埋藏于此山之下，灵气浓郁，引得无数修士探险。', false, 12, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (12, 31);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (12, 32);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (12, 1004); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (13, '魔气深渊', 36, '元婴七层', '青龙域', '魔气深渊', 'bg_dark_1', '远古大战遗留的魔气汇聚之地，魔修与妖魔盘踞。', false, 13, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (13, 31);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (13, 32);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (13, 1004); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (14, '南海龙宫', 40, '化神一层', '南海', '龙宫外围', 'bg_water_1', '海底龙族宫殿，蕴含无尽宝藏，但也有凶猛的海兽守护。', false, 14, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (14, 41);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (14, 42);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (14, 1005); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (15, '珊瑚海域', 43, '化神四层', '南海', '珊瑚海域', 'bg_water_1', '五彩珊瑚遍布，灵鱼群游，但也有巨大的海妖潜伏。', false, 15, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (15, 41);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (15, 42);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (15, 1005); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (16, '幽冥海沟', 46, '化神七层', '南海', '幽冥海沟', 'bg_dark_1', '深不见底的海沟，阳光无法抵达，传说通往冥界。', false, 16, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (16, 41);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (16, 42);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (16, 1005); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (17, '极寒冰原', 50, '炼虚一层', '北冥', '极寒冰原', 'bg_ice_1', '万载不化的玄冰覆盖此地，灵气近乎凝固。唯有大神通者方能自保。', false, 17, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (17, 51);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (17, 52);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (17, 1006); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (18, '冰封雪山', 53, '炼虚四层', '北冥', '冰封雪山', 'bg_ice_1', '终年积雪的巍峨雪山，山顶有上古冰凤巢穴。', false, 18, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (18, 51);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (18, 52);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (18, 1006); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (19, '寒潭幽谷', 56, '炼虚七层', '北冥', '寒潭幽谷', 'bg_ice_1', '谷中寒潭千年不化，传说有冰属性天材地宝。', false, 19, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (19, 51);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (19, 52);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (19, 1006); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (20, '虚空裂缝', 60, '合体一层', '虚空', '裂缝边缘', 'bg_celestial_1', '位面交界处的裂缝，时空错乱，虚空生物出没。', false, 20, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (20, 61);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (20, 62);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (20, 1007); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (21, '混沌边境', 63, '合体四层', '虚空', '混沌边境', 'bg_celestial_1', '混沌与秩序交汇之地，法则不稳，危机与机遇并存。', false, 21, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (21, 61);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (21, 62);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (21, 1007); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (22, '星辰墓地', 66, '合体七层', '虚空', '星辰墓地', 'bg_celestial_1', '陨落星辰的安息之地，蕴含恐怖的星辰之力。', false, 22, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (22, 61);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (22, 62);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (22, 1007); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (23, '天劫雷海', 70, '大乘一层', '天劫域', '雷海外围', 'bg_dark_1', '位面边缘，雷霆肆虐。此处是修士感悟天道的磨刀石。', false, 23, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (23, 71);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (23, 72);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (23, 1008); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (24, '天雷峰', 73, '大乘四层', '天劫域', '天雷峰', 'bg_mountain_1', '终年雷电不断的山峰，修炼雷法的圣地。', false, 24, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (24, 71);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (24, 72);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (24, 1008); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (25, '紫霄神殿', 76, '大乘七层', '天劫域', '紫霄神殿', 'bg_celestial_1', '远古大能遗留的神殿，蕴含无上道韵。', false, 25, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (25, 71);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (25, 72);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (25, 1008); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (26, '天劫核心', 80, '渡劫一层', '天劫域', '劫核心', 'bg_celestial_1', '天劫最核心区域，雷霆法则具象化为生物。', false, 26, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (26, 81);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (26, 82);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (26, 1009); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (27, '仙凡通道', 83, '渡劫四层', '天劫域', '仙凡通道', 'bg_celestial_1', '连接凡界与仙界的通道，仙气与凡气交汇。', false, 27, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (27, 81);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (27, 82);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (27, 1009); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (28, '飞升台', 86, '渡劫七层', '天劫域', '飞升台', 'bg_celestial_1', '历代飞升者最后踏足凡界的地方，蕴含飞升法则。', false, 28, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (28, 81);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (28, 82);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (28, 1009); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (101, '升仙池', 90, '人仙初期', '仙界', '升仙池', 'bg_water_1', '飞升后的第一站，洗去凡胎尘埃，凝练仙灵之躯。', true, 101, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (101, 91);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (101, 92);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (101, 2001); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (102, '仙灵森林', 91, '人仙中期', '仙界', '仙灵森林', 'bg_forest_1', '仙界边缘的灵木之森，灵气充沛，仙草遍地。', true, 102, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (102, 91);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (102, 92);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (102, 2001); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (103, '蓬莱仙岛', 93, '地仙初期', '仙界', '蓬莱仙岛', 'bg_water_1', '海外仙山之首，岛上奇草遍地，灵兽成群。乃仙人汇聚之地。', true, 103, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (103, 93);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (103, 94);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (103, 2002); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (104, '瀛洲岛', 94, '地仙中期', '仙界', '瀛洲岛', 'bg_water_1', '三仙岛之一，岛上有长生不老草。', true, 104, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (104, 93);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (104, 94);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (104, 2002); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (105, '方丈岛', 95, '地仙后期', '仙界', '方丈岛', 'bg_water_1', '三仙岛之一，岛上灵泉众多，可洗髓伐骨。', true, 105, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (105, 93);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (105, 94);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (105, 2002); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (106, '九重天庭', 96, '天仙初期', '仙界', '九重天庭', 'bg_celestial_1', '威严宏伟的仙界权力中心，金砖铺路，玉柱擎天。', true, 106, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (106, 95);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (106, 96);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (106, 2003); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (107, '兜率宫', 97, '天仙中期', '仙界', '兜率宫', 'bg_celestial_1', '太上老君炼丹之所，仙丹灵药无数。', true, 107, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (107, 95);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (107, 96);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (107, 2003); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (108, '凌霄殿', 98, '天仙后期', '仙界', '凌霄殿', 'bg_celestial_1', '天庭最高殿堂，天帝议事之所。', true, 108, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (108, 95);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (108, 96);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (108, 2003); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (109, '混沌海', 99, '金仙初期', '仙界', '混沌海', 'bg_celestial_1', '宇宙诞生之初的混沌之海，蕴含创世法则。', true, 109, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (109, 97);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (109, 98);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (109, 2004); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (110, '无量神山', 100, '金仙中期', '仙界', '无量神山', 'bg_mountain_1', '无量尊者道场，高耸入云，直通大罗天。', true, 110, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (110, 97);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (110, 98);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (110, 2004); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (111, '大罗天', 101, '金仙后期', '仙界', '大罗天', 'bg_celestial_1', '大罗金仙居所，俯瞰三界，超脱轮回。', true, 111, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (111, 97);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (111, 98);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (111, 2004); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (112, '道祖圣地', 102, '大罗金仙初期', '仙界', '道祖圣地', 'bg_celestial_1', '道祖证道之地，蕴含无上大道。', true, 112, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (112, 99);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (112, 100);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (112, 2005); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (113, '紫霄宫', 103, '大罗金仙中期', '仙界', '紫霄宫', 'bg_celestial_1', '鸿钧道祖讲道之所，三清证道之地。', true, 113, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (113, 99);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (113, 100);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (113, 2005); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (114, '混沌祖地', 104, '大罗金仙后期', '仙界', '混沌祖地', 'bg_celestial_1', '混沌本源之地，宇宙起源的奥秘尽在其中。', true, 114, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (114, 99);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (114, 100);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (114, 2005); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (115, '三十三天', 105, '道祖初期', '诸天', '三十三天', 'bg_celestial_1', '诸天之上，道祖执掌之地。', true, 115, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (115, 101);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (115, 102);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (115, 2006); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (116, '大道天宫', 106, '道祖中期', '诸天', '大道天宫', 'bg_celestial_1', '天道化身居所，掌控三界六道轮回。', true, 116, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (116, 101);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (116, 102);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (116, 2006); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (117, '永恒神殿', 107, '道祖后期', '诸天', '永恒神殿', 'bg_celestial_1', '超脱时空的永恒存在，修仙的终极之地。', true, 117, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (117, 101);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (117, 102);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (117, 2006); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (201, '上古战场', 10, '炼气/筑基', '秘境', '上古战场', 'bg_desert_1', '上古时期仙魔大战的遗迹，虽然凶险但蕴含无数机缘。掉落大量基础材料。', false, 201, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (201, 11);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (201, 12);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (201, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (201, 1002); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (202, '真龙巢穴', 40, '金丹/元婴', '秘境', '真龙巢穴', 'bg_mountain_1', '传说中真龙栖息之地，如今已被亚龙与妖兽占据。掉落高阶装备。', false, 202, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (202, 31);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (202, 41);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (202, 21);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (202, 1004); -- Boss
INSERT INTO t_cfg_map (id, name, req_realm_id, recommend_realm, region, sub_region, bg_image, description, is_immortal, sort_order, is_active) VALUES (203, '通天塔遗迹', 70, '合体/大乘', '秘境', '通天塔遗迹', 'bg_celestial_1', '曾经通往仙界的通天塔废墟，空间乱流肆虐。掉落极品强化材料。', false, 203, 1);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (203, 61);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (203, 71);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (203, 97);
INSERT INTO t_cfg_map_monster (map_id, monster_id) VALUES (203, 1008); -- Boss
