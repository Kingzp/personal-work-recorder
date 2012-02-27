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
       VALUES (1, strftime('%s', date('now', '-34 days')||' 09:12:11', 'utc') * 1000,
                  strftime('%s', date('now', '-34 days')||' 17:52:29', 'utc') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (2, strftime('%s', date('now', '-33 days')||' 08:52:20', 'utc') * 1000,
                  strftime('%s', date('now', '-33 days')||' 18:42:51', 'utc') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (3, strftime('%s', date('now', '-32 days')||' 08:22:10', 'utc') * 1000,
                  strftime('%s', date('now', '-32 days')||' 19:17:42', 'utc') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (4, strftime('%s', date('now', '-31 days')||' 10:24:10', 'utc') * 1000,
                  strftime('%s', date('now', '-31 days')||' 20:11:50', 'utc') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (5, strftime('%s', date('now', '-8 days')||' 10:01:27', 'utc') * 1000,
                  strftime('%s', date('now', '-8 days')||' 19:21:10', 'utc') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (6, strftime('%s', date('now', '-5 days')||' 09:12:41', 'utc') * 1000,
                  strftime('%s', date('now', '-5 days')||' 20:01:27', 'utc') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (7, strftime('%s', date('now', '-4 days')||' 08:27:12', 'utc') * 1000,
                  strftime('%s', date('now', '-4 days')||' 18:34:36', 'utc') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (8, strftime('%s', date('now', '-3 days')||' 09:00:00', 'utc') * 1000,
                  strftime('%s', date('now', '-3 days')||' 18:30:00', 'utc') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (9, strftime('%s', date('now', '-2 days')||' 10:00:00', 'utc') * 1000,
                  strftime('%s', date('now', '-2 days')||' 22:30:00', 'utc') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (10, strftime('%s', date('now', '-1 days')||' 11:00:00', 'utc') * 1000,
                  strftime('%s', date('now')||' 02:00:00', 'utc') * 1000);
INSERT INTO work_records (_id, start_time, end_time)
       VALUES (11, strftime('%s', date('now')||' 10:00:00', 'utc') * 1000, NULL);
EOF

sqlite3 pwr.db <<EOF
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (1, strftime('%s', date('now', '-34 days')||' 09:12:11', 'utc') * 1000, 'A01', 'ミーティング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (1, strftime('%s', date('now', '-34 days')||' 09:41:57', 'utc') * 1000, 'B02', 'コーディング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (1, strftime('%s', date('now', '-34 days')||' 11:45:19', 'utc') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (1, strftime('%s', date('now', '-34 days')||' 12:44:01', 'utc') * 1000, 'B03', 'テスト');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (1, strftime('%s', date('now', '-34 days')||' 16:02:21', 'utc') * 1000, 'A02', '進捗報告会');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (1, strftime('%s', date('now', '-34 days')||' 17:11:07', 'utc') * 1000, 'E01', '本番準備');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (2, strftime('%s', date('now', '-33 days')||' 08:52:20', 'utc') * 1000, 'B01', '詳細設計');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (2, strftime('%s', date('now', '-33 days')||' 09:27:01', 'utc') * 1000, 'F01', '問い合せ対応');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (2, strftime('%s', date('now', '-33 days')||' 14:07:22', 'utc') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (2, strftime('%s', date('now', '-33 days')||' 15:22:41', 'utc') * 1000, 'B01', '詳細設計');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (3, strftime('%s', date('now', '-32 days')||' 08:22:10', 'utc') * 1000, 'B02', 'コーディング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (3, strftime('%s', date('now', '-32 days')||' 10:17:22', 'utc') * 1000, 'B03', 'テスト');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (3, strftime('%s', date('now', '-32 days')||' 12:30:01', 'utc') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (3, strftime('%s', date('now', '-32 days')||' 13:11:27', 'utc') * 1000, 'B03', 'テスト');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (3, strftime('%s', date('now', '-32 days')||' 17:02:22', 'utc') * 1000, 'A02', '進捗報告会');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (3, strftime('%s', date('now', '-32 days')||' 18:24:06', 'utc') * 1000, 'D01', '事務作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (4, strftime('%s', date('now', '-31 days')||' 10:24:10', 'utc') * 1000, 'F01', '問い合せ対応');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (4, strftime('%s', date('now', '-31 days')||' 12:10:01', 'utc') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (4, strftime('%s', date('now', '-31 days')||' 13:02:41', 'utc') * 1000, 'A01', 'ミーティング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (4, strftime('%s', date('now', '-31 days')||' 14:05:17', 'utc') * 1000, 'B01', '詳細設計');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (4, strftime('%s', date('now', '-31 days')||' 17:11:06', 'utc') * 1000, 'B02', 'コーディング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (5, strftime('%s', date('now', '-8 days')||' 10:01:27', 'utc') * 1000, 'A01', 'ミーティング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (5, strftime('%s', date('now', '-8 days')||' 10:27:52', 'utc') * 1000, 'E02', '本番リリース作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (5, strftime('%s', date('now', '-8 days')||' 12:27:11', 'utc') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (5, strftime('%s', date('now', '-8 days')||' 13:17:27', 'utc') * 1000, 'E02', '本番リリース作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (6, strftime('%s', date('now', '-5 days')||' 09:12:41', 'utc') * 1000, 'D01', '事務作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (6, strftime('%s', date('now', '-5 days')||' 10:01:22', 'utc') * 1000, 'B02', 'コーディング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (6, strftime('%s', date('now', '-5 days')||' 12:11:07', 'utc') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (6, strftime('%s', date('now', '-5 days')||' 13:03:17', 'utc') * 1000, 'E01', '本番準備');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (6, strftime('%s', date('now', '-5 days')||' 16:50:01', 'utc') * 1000, 'A02', '進捗報告会');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (6, strftime('%s', date('now', '-5 days')||' 17:57:22', 'utc') * 1000, 'B03', 'テスト');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (7, strftime('%s', date('now', '-4 days')||' 08:27:12', 'utc') * 1000, 'A02', '進捗報告会');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (7, strftime('%s', date('now', '-4 days')||' 10:57:22', 'utc') * 1000, 'D01', '事務作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (7, strftime('%s', date('now', '-4 days')||' 11:50:27', 'utc') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (7, strftime('%s', date('now', '-4 days')||' 12:47:17', 'utc') * 1000, 'B01', '詳細設計');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (7, strftime('%s', date('now', '-4 days')||' 15:22:01', 'utc') * 1000, 'F01', '問い合せ対応');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (7, strftime('%s', date('now', '-4 days')||' 17:00:27', 'utc') * 1000, 'E01', '本番準備');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (8, strftime('%s', date('now', '-3 days')||' 09:00:00', 'utc') * 1000, 'A01', 'ミーティング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (8, strftime('%s', date('now', '-3 days')||' 11:00:00', 'utc') * 1000, 'A02', '進捗報告会');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (8, strftime('%s', date('now', '-3 days')||' 12:00:00', 'utc') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (8, strftime('%s', date('now', '-3 days')||' 13:00:00', 'utc') * 1000, 'B03', 'テスト');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (8, strftime('%s', date('now', '-3 days')||' 17:00:00', 'utc') * 1000, 'D01', '事務作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (8, strftime('%s', date('now', '-3 days')||' 18:00:00', 'utc') * 1000, 'B01', '詳細設計');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (9, strftime('%s', date('now', '-2 days')||' 10:00:00', 'utc') * 1000, 'A01', 'ミーティング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (9, strftime('%s', date('now', '-2 days')||' 10:30:00', 'utc') * 1000, 'D01', '事務作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (9, strftime('%s', date('now', '-2 days')||' 11:00:00', 'utc') * 1000, 'B01', '詳細設計');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (9, strftime('%s', date('now', '-2 days')||' 12:00:00', 'utc') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (9, strftime('%s', date('now', '-2 days')||' 13:00:00', 'utc') * 1000, 'B01', '詳細設計');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (9, strftime('%s', date('now', '-2 days')||' 17:00:00', 'utc') * 1000, 'A01', 'ミーティング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (9, strftime('%s', date('now', '-2 days')||' 18:00:00', 'utc') * 1000, 'F01', '本番リリース作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (10, strftime('%s', date('now', '-1 days')||' 11:00:00', 'utc') * 1000, 'A01', 'ミーティング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (10, strftime('%s', date('now', '-1 days')||' 12:00:00', 'utc') * 1000, 'C01', '休憩');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (10, strftime('%s', date('now', '-1 days')||' 13:00:00', 'utc') * 1000, 'A02', '進捗報告会');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (10, strftime('%s', date('now', '-1 days')||' 15:00:00', 'utc') * 1000, 'B03', 'テスト');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (10, strftime('%s', date('now', '-1 days')||' 19:00:00', 'utc') * 1000, 'F02', '本番リリース作業');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (11, strftime('%s', date('now')||' 10:00:00', 'utc') * 1000, 'A01', 'ミーティング');
INSERT INTO task_records (work_id, start_time, code, description)
        VALUES (11, strftime('%s', date('now')||' 11:00:00', 'utc') * 1000, 'D01', '事務作業');
EOF

