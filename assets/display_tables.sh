# HOW TO USE
# 1. copy this script to /data/data/chokoapp.imanani/databases by DDMS tool
# 2. at adb shell. type
#    # cd /data/data/chokoapp.imanani/databases (the place where the database file is stored)
#    # chmod 776 display_tables.sh (not be accepted u+x style, you must specify hex code)
# 3. execute this script
#    # ./display_tables.sh
#
#!/system/bin/sh

SELECT_TASKS="SELECT * FROM tasks;"
SELECT_WORK_RECORDS="SELECT _id, \
                            datetime(start_time / 1000, 'unixepoch', 'localtime') 'start', \
                            datetime(end_time / 1000, 'unixepoch', 'localtime') 'end' \
                     FROM work_records;"
SELECT_TASK_RECORDS="SELECT work_id w_id, \
                            task_records._id t_id, \
                            datetime(work_records.start_time / 1000, 'unixepoch', 'localtime') 'work_start', \
                            time(task_records.start_time / 1000, 'unixepoch', 'localtime') 'task_start', \
                            CASE WHEN EXISTS (SELECT * FROM task_records t_end \
                                               WHERE task_records.work_id = t_end.work_id \
                                                 AND task_records.start_time < t_end.start_time) \
                                 THEN (SELECT time(min(t_end.start_time) / 1000, 'unixepoch', 'localtime') \
                                         FROM task_records t_end \
                                        WHERE task_records.work_id = t_end.work_id \
                                          AND task_records.start_time < t_end.start_time) \
                                 ELSE time(work_records.end_time / 1000, 'unixepoch', 'localtime') END 'task_end', \
                            code, description \
                       FROM work_records LEFT OUTER JOIN task_records \
                         ON work_id = work_records._id;"

echo "tm)tasks, wr)work_records, tr)task_records, q)quit"
echo
while read TABLE_NAME; do
    case $TABLE_NAME in
        tm|tasks)
            sqlite3 -header -column pwr.db "$SELECT_TASKS"
            ;;
        wr|work_records)
            sqlite3 -header -column pwr.db "$SELECT_WORK_RECORDS"
            ;;
        tr|task_records)
            sqlite3 -header -column pwr.db "$SELECT_TASK_RECORDS"
            ;;
        q|quit)
            break
            ;;
        *) echo "you must specify a table name tasks, work_records or task_records"
            ;;
    esac

    echo "tm)tasks, wr)work_records, tr)task_records, q)quit"
    echo
done
exit 0
