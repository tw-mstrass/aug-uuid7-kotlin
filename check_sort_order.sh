#!/usr/bin/env bash

set -euo pipefail

if ! command -v csvq &> /dev/null; then
    echo "Error: csvq executable not found on the path"
    echo "Please install csvq: https://github.com/mithrandie/csvq"
    exit 1
fi

CSVFILE=uuid7_data.csv

csvq "select * from \`${CSVFILE}\` where original <> sorted order by rownum; select count(*) from \`${CSVFILE}\` where original <> sorted"
