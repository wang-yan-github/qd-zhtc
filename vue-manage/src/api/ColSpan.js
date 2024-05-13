import {ref} from "vue"

/**
 * 合并表格公告js
 */
export function useColSpan() {
    const spanArr = ref([]);

    const objectSpanMethod = ({row, column, rowIndex, columnIndex}) => {
        if (![2, 3, 4].includes(columnIndex)) {
            /*
            将需要合并的行数赋值给 _row，注意这里由上一个方法的输出可以知道，这里的值可以是 3或者0
            当为 3 时，表示将当下的第 rowIndex+1 行与第 columnIndex+1 列所指向的单元格向下合并 _row 格
            当为 0 时，表示将当下的第 rowIndex+1 行与第 columnIndex+1 列所指向的单元格隐藏
            */
            const _row = spanArr.value[rowIndex];
            const _col = _row > 0 ? 1 : 0;
            return {
                rowspan: _row,
                colspan: _col
            }
        }
    };

    const getSpanArr = (data) => {
        const spanList = [];
        let pos = 0;
        // 遍历数据
        for (let i = 0; i < data.length; i++) {
            // 如果是第一个数据，就将列表spanArr添加一个1，表示暂时只有一个名字相同的、且将索引pos赋值为0
            if (i === 0) {
                spanList.push(1);
                pos = 0
            } else {
                // 判断当前元素与上一个元素是否相同
                if (data[i].title === data[i - 1].title) {
                    // 如果相同就将索引为 pos 的值加一
                    spanList[pos] += 1;
                    // 且将数组添加 0
                    spanList.push(0);
                } else {
                    // 如果元素不同了，就可以通过索引为 pos 的值知晓应该需要合并的行数
                    // 同时，我们再次添加一个值1，表示重新开始判断重复姓名的次数
                    spanList.push(1);
                    // 同时 索引加一
                    pos = i;
                }
            }
        }
        spanArr.value = spanList
    };

    return {spanArr, objectSpanMethod, getSpanArr}
}
