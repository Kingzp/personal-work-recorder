#!/system/bin/sh

sqlite3 pwr.db "DELETE FROM tasks;"
sqlite3 pwr.db "DELETE FROM work_records;"
sqlite3 pwr.db "DELETE FROM task_records;"
sqlite3 pwr.db "DELETE FROM daily_work_summary"
sqlite3 pwr.db "DELETE FROM daily_task_summary"

sqlite3 pwr.db <<EOF
INSERT INTO tasks (code, description) VALUES ('A01', 'ミーティング');
INSERT INTO tasks (code, description) VALUES ('A02', '進捗報告会');
INSERT INTO tasks (code, description) VALUES ('B01', '詳細設計');
INSERT INTO tasks (code, description) VALUES ('B02', 'コーディング');
INSERT INTO tasks (code, description) VALUES ('B03', 'テスト');
INSERT INTO tasks (code, description) VALUES ('C01', '休憩');
INSERT INTO tasks (code, description) VALUES ('D01', '事務作業');
INSERT INTO tasks (code, description) VALUES ('E01', '本番準備');
INSERT INTO tasks (code, description) VALUES ('E02', '本番リリース作業');
INSERT INTO tasks (code, description) VALUES ('F01', '問い合せ対応');
EOF

sqlite3 pwr.db <<EOF
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (1, strftime('%s', date('now', '-34 days')||' 01:12:11') * 1000,
                  strftime('%s', date('now', '-34 days')||' 08:52:29') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (2, strftime('%s', date('now', '-34 days')||' 23:52:20') * 1000,
                  strftime('%s', date('now', '-33 days')||' 09:42:51') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (3, strftime('%s', date('now', '-33 days')||' 23:22:10') * 1000,
                  strftime('%s', date('now', '-32 days')||' 10:17:42') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (4, strftime('%s', date('now', '-31 days')||' 01:24:10') * 1000,
                  strftime('%s', date('now', '-31 days')||' 11:11:50') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (5, strftime('%s', date('now', '-8 days')||' 01:01:27') * 1000,
                  strftime('%s', date('now', '-8 days')||' 10:21:10') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (6, strftime('%s', date('now', '-5 days')||' 00:12:41') * 1000,
                  strftime('%s', date('now', '-5 days')||' 11:01:27') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (7, strftime('%s', date('now', '-5 days')||' 23:27:12') * 1000,
                  strftime('%s', date('now', '-4 days')||' 09:34:36') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (8, strftime('%s', date('now', '-3 days')||' 00:00:00') * 1000,
                  strftime('%s', date('now', '-3 days')||' 09:30:00') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (9, strftime('%s', date('now', '-2 days')||' 01:00:00') * 1000,
                  strftime('%s', date('now', '-2 days')||' 13:30:00') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (10, strftime('%s', date('now', '-1 days')||' 02:00:00') * 1000,
                  strftime('%s', date('now', '-1 days')||' 17:00:00') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (11, strftime('%s', date('now')||' 01:00:00') * 1000, NULL);
EOF

sqlite3 pwr.db <<EOF
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (1, strftime('%s', date('now', '-34 days')||' 00:12:11') * 1000, 'A01', 'ミーティング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (1, strftime('%s', date('now', '-34 days')||' 00:41:57') * 1000, 'B02', 'コーディング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (1, strftime('%s', date('now', '-34 days')||' 02:45:19') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (1, strftime('%s', date('now', '-34 days')||' 03:44:01') * 1000, 'B03', 'テスト');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (1, strftime('%s', date('now', '-34 days')||' 07:02:21') * 1000, 'A02', '進捗報告会');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (1, strftime('%s', date('now', '-34 days')||' 08:11:07') * 1000, 'E01', '本番準備');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (2, strftime('%s', date('now', '-34 days')||' 23:52:20') * 1000, 'B01', '詳細設計');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (2, strftime('%s', date('now', '-33 days')||' 00:27:01') * 1000, 'F01', '問い合せ対応');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (2, strftime('%s', date('now', '-33 days')||' 05:07:22') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (2, strftime('%s', date('now', '-33 days')||' 06:22:41') * 1000, 'B01', '詳細設計');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (3, strftime('%s', date('now', '-33 days')||' 23:22:10') * 1000, 'B02', 'コーディング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (3, strftime('%s', date('now', '-32 days')||' 01:17:22') * 1000, 'B03', 'テスト');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (3, strftime('%s', date('now', '-32 days')||' 03:30:01') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (3, strftime('%s', date('now', '-32 days')||' 04:11:27') * 1000, 'B03', 'テスト');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (3, strftime('%s', date('now', '-32 days')||' 08:02:22') * 1000, 'A02', '進捗報告会');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (3, strftime('%s', date('now', '-32 days')||' 09:24:06') * 1000, 'D01', '事務作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (4, strftime('%s', date('now', '-31 days')||' 01:24:10') * 1000, 'F01', '問い合せ対応');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (4, strftime('%s', date('now', '-31 days')||' 03:10:01') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (4, strftime('%s', date('now', '-31 days')||' 04:02:41') * 1000, 'A01', 'ミーティング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (4, strftime('%s', date('now', '-31 days')||' 05:05:17') * 1000, 'B01', '詳細設計');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (4, strftime('%s', date('now', '-31 days')||' 08:11:06') * 1000, 'B02', 'コーディング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (5, strftime('%s', date('now', '-8 days')||' 01:01:27') * 1000, 'A01', 'ミーティング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (5, strftime('%s', date('now', '-8 days')||' 01:27:52') * 1000, 'E02', '本番リリース作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (5, strftime('%s', date('now', '-8 days')||' 03:27:11') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (5, strftime('%s', date('now', '-8 days')||' 04:17:27') * 1000, 'E02', '本番リリース作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (6, strftime('%s', date('now', '-5 days')||' 00:12:41') * 1000, 'D01', '事務作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (6, strftime('%s', date('now', '-5 days')||' 01:01:22') * 1000, 'B02', 'コーディング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (6, strftime('%s', date('now', '-5 days')||' 03:11:07') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (6, strftime('%s', date('now', '-5 days')||' 04:03:17') * 1000, 'E01', '本番準備');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (6, strftime('%s', date('now', '-5 days')||' 07:50:01') * 1000, 'A02', '進捗報告会');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (6, strftime('%s', date('now', '-5 days')||' 08:57:22') * 1000, 'B03', 'テスト');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (7, strftime('%s', date('now', '-5 days')||' 23:27:12') * 1000, 'A02', '進捗報告会');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (7, strftime('%s', date('now', '-4 days')||' 01:57:22') * 1000, 'D01', '事務作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (7, strftime('%s', date('now', '-4 days')||' 02:50:27') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (7, strftime('%s', date('now', '-4 days')||' 03:47:17') * 1000, 'B01', '詳細設計');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (7, strftime('%s', date('now', '-4 days')||' 06:22:01') * 1000, 'F01', '問い合せ対応');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (7, strftime('%s', date('now', '-4 days')||' 08:00:27') * 1000, 'E01', '本番準備');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (8, strftime('%s', date('now', '-3 days')||' 00:00:00') * 1000, 'A01', 'ミーティング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (8, strftime('%s', date('now', '-3 days')||' 02:00:00') * 1000, 'A02', '進捗報告会');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (8, strftime('%s', date('now', '-3 days')||' 03:00:00') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (8, strftime('%s', date('now', '-3 days')||' 04:00:00') * 1000, 'B03', 'テスト');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (8, strftime('%s', date('now', '-3 days')||' 08:00:00') * 1000, 'D01', '事務作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (8, strftime('%s', date('now', '-3 days')||' 09:00:00') * 1000, 'B01', '詳細設計');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (9, strftime('%s', date('now', '-2 days')||' 01:00:00') * 1000, 'A01', 'ミーティング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (9, strftime('%s', date('now', '-2 days')||' 01:30:00') * 1000, 'D01', '事務作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (9, strftime('%s', date('now', '-2 days')||' 02:00:00') * 1000, 'B01', '詳細設計');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (9, strftime('%s', date('now', '-2 days')||' 03:00:00') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (9, strftime('%s', date('now', '-2 days')||' 04:00:00') * 1000, 'B01', '詳細設計');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (9, strftime('%s', date('now', '-2 days')||' 08:00:00') * 1000, 'A01', 'ミーティング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (9, strftime('%s', date('now', '-2 days')||' 09:00:00') * 1000, 'F01', '本番リリース作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (10, strftime('%s', date('now', '-1 days')||' 02:00:00') * 1000, 'A01', 'ミーティング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (10, strftime('%s', date('now', '-1 days')||' 03:00:00') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (10, strftime('%s', date('now', '-1 days')||' 04:00:00') * 1000, 'A02', '進捗報告会');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (10, strftime('%s', date('now', '-1 days')||' 06:00:00') * 1000, 'B03', 'テスト');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (10, strftime('%s', date('now', '-1 days')||' 10:00:00') * 1000, 'F02', '本番リリース作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (11, strftime('%s', date('now')||' 01:00:00') * 1000, 'A01', 'ミーティング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (11, strftime('%s', date('now')||' 02:00:00') * 1000, 'D01', '事務作業');
EOF

