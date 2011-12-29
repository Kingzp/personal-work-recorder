#!/system/bin/sh

sqlite3 pwr.db "DELETE FROM tasks;"
sqlite3 pwr.db "DELETE FROM work_records;"
sqlite3 pwr.db "DELETE FROM task_records;"

sqlite3 pwr.db <<EOF
INSERT INTO tasks (code, description) VALUES ('A01', 'ミーティング');
INSERT INTO tasks (code, description) VALUES ('A02', '進捗報告会');
INSERT INTO tasks (code, description) VALUES ('B01', '詳細設計');
INSERT INTO tasks (code, description) VALUES ('B02', 'コーディング');
INSERT INTO tasks (code, description) VALUES ('B03', 'テスト');
INSERT INTO tasks (code, description) VALUES ('C01', '休憩');
INSERT INTO tasks (code, description) VALUES ('D01', '事務作業');
EOF

sqlite3 pwr.db <<EOF
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (1, strftime('%s', '2011-12-01 09:00:00', 'utc') * 1000,
                  strftime('%s', '2011-12-01 18:30:00', 'utc') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (2, strftime('%s', '2011-12-02 10:00:00', 'utc') * 1000,
                  strftime('%s', '2011-12-02 22:30:00', 'utc') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (3, strftime('%s', '2011-12-03 11:00:00', 'utc') * 1000,
                  strftime('%s', '2011-12-04 02:00:00', 'utc') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (4, strftime('%s', '2011-12-04 10:00:00', 'utc') * 1000, NULL);
EOF

sqlite3 pwr.db <<EOF
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (1, strftime('%s', '2011-12-01 09:00:00', 'utc') * 1000, 'A01', 'ミーティング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (1, strftime('%s', '2011-12-01 11:00:00', 'utc') * 1000, 'A02', '進捗報告会');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (1, strftime('%s', '2011-12-01 12:00:00', 'utc') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (1, strftime('%s', '2011-12-01 13:00:00', 'utc') * 1000, 'B03', 'テスト');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (1, strftime('%s', '2011-12-01 17:00:00', 'utc') * 1000, 'D01', '事務作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (1, strftime('%s', '2011-12-01 18:00:00', 'utc') * 1000, 'B01', '詳細設計');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (2, strftime('%s', '2011-12-02 10:00:00', 'utc') * 1000, 'A01', 'ミーティング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (2, strftime('%s', '2011-12-02 10:30:00', 'utc') * 1000, 'D01', '事務作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (2, strftime('%s', '2011-12-02 11:00:00', 'utc') * 1000, 'B01', '詳細設計');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (2, strftime('%s', '2011-12-02 12:00:00', 'utc') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (2, strftime('%s', '2011-12-02 13:00:00', 'utc') * 1000, 'B01', '詳細設計');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (2, strftime('%s', '2011-12-02 17:00:00', 'utc') * 1000, 'A01', 'ミーティング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (2, strftime('%s', '2011-12-02 18:00:00', 'utc') * 1000, 'F01', '本番リリース作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (3, strftime('%s', '2011-12-03 11:00:00', 'utc') * 1000, 'A01', 'ミーティング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (3, strftime('%s', '2011-12-03 12:00:00', 'utc') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (3, strftime('%s', '2011-12-03 13:00:00', 'utc') * 1000, 'A02', '進捗報告会');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (3, strftime('%s', '2011-12-03 15:00:00', 'utc') * 1000, 'B03', 'テスト');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (3, strftime('%s', '2011-12-03 19:00:00', 'utc') * 1000, 'F02', '本番リリース作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (4, strftime('%s', '2011-12-04 10:00:00', 'utc') * 1000, 'A01', 'ミーティング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (4, strftime('%s', '2011-12-04 11:00:00', 'utc') * 1000, 'D01', '事務作業');
EOF

