var itemStyle1 = [
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [{offset: 0, color: '#f73d76' /* 0% 处的颜色*/}, {offset: 1, color: '#b152ff' /* 100% 处的颜色*/}],
                globalCoord: false /* 缺省为 false*/
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [{offset: 0, color: '#ff9090' /* 0% 处的颜色*/}, {offset: 1, color: '#fdc17c' /* 100% 处的颜色*/}],
                globalCoord: false /* 缺省为 false*/
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    }
];

var itemStyle2 = [
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [{offset: 0, color: '#d453f6' /* 0% 处的颜色*/}, {offset: 1, color: '#5f66f3' /* 100% 处的颜色*/}],
                globalCoord: false /* 缺省为 false*/
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [{offset: 0, color: '#51c6ee' /* 0% 处的颜色*/}, {offset: 1, color: '#52f199' /* 100% 处的颜色*/}],
                globalCoord: false /* 缺省为 false*/
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [{offset: 0, color: '#50b055' /* 0% 处的颜色*/}, {offset: 1, color: '#daf163' /* 100% 处的颜色*/}],
                globalCoord: false /* 缺省为 false*/
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [{offset: 0, color: '#ffb199' /* 0% 处的颜色*/}, {offset: 1, color: '#ff0844' /* 100% 处的颜色*/}],
                globalCoord: false /* 缺省为 false*/
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [{offset: 0, color: '#ff6c69' /* 0% 处的颜色*/}, {offset: 1, color: '#eb9c60' /* 100% 处的颜色*/}],
                globalCoord: false /* 缺省为 false*/
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [{offset: 0, color: 'white' /* 0% 处的颜色*/}, {offset: 1, color: 'gray' /* 100% 处的颜色*/}],
                globalCoord: false /* 缺省为 false*/
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    }
];

var itemStyle3 = [
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [{offset: 0, color: '#ffffb6' /* 0% 处的颜色*/}, {offset: 1, color: '#ff00b6' /* 100% 处的颜色*/}]
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [{offset: 0, color: '#1ad1fb' /* 0% 处的颜色*/}, {offset: 1, color: '#1e66f0' /* 100% 处的颜色*/}]
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [{offset: 0, color: '#80da9a' /* 0% 处的颜色*/}, {offset: 1, color: '#1bb1a1' /* 100% 处的颜色*/}]
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [{offset: 0, color: '#f73d76' /* 0% 处的颜色*/}, {offset: 1, color: '#b152ff' /* 100% 处的颜色*/}]
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [{offset: 0, color: '#ff9090' /* 0% 处的颜色*/}, {offset: 1, color: '#fdc17c' /* 100% 处的颜色*/}]
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [{offset: 0, color: '#50b055' /* 0% 处的颜色*/}, {offset: 1, color: '#daf163' /* 100% 处的颜色*/}]
            },
            shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [{offset: 0, color: '#fb7d64' /* 0% 处的颜色*/}, {offset: 1, color: '#f93b67' /* 100% 处的颜色*/}]
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [{offset: 0, color: '#26a8e6' /* 0% 处的颜色*/}, {offset: 1, color: '#1ec5af' /* 100% 处的颜色*/}]
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [{offset: 0, color: '#e251b6' /* 0% 处的颜色*/}, {offset: 1, color: '#b72bee' /* 100% 处的颜色*/}]
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [{offset: 0, color: '#ffb199' /* 0% 处的颜色*/}, {offset: 1, color: '#ff0844' /* 100% 处的颜色*/}]
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [{offset: 0, color: '#ff6c69' /* 0% 处的颜色*/}, {offset: 1, color: '#eb9c60' /* 100% 处的颜色*/}]
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    }
];


var itemStyle3_horizontal = [
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 1, y2: 1,
                colorStops: [{offset: 0, color: '#ffffb6' /* 0% 处的颜色*/}, {offset: 1, color: '#ff00b6' /* 100% 处的颜色*/}]
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 1, y2: 1,
                colorStops: [{offset: 0, color: '#1ad1fb' /* 0% 处的颜色*/}, {offset: 1, color: '#1e66f0' /* 100% 处的颜色*/}]
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 1, y2: 1,
                colorStops: [{offset: 0, color: '#80da9a' /* 0% 处的颜色*/}, {offset: 1, color: '#1bb1a1' /* 100% 处的颜色*/}]
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 1, y2: 1,
                colorStops: [{offset: 0, color: '#f73d76' /* 0% 处的颜色*/}, {offset: 1, color: '#b152ff' /* 100% 处的颜色*/}]
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 1, y2: 1,
                colorStops: [{offset: 0, color: '#ff9090' /* 0% 处的颜色*/}, {offset: 1, color: '#fdc17c' /* 100% 处的颜色*/}]
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 1, y2: 1,
                colorStops: [{offset: 0, color: '#50b055' /* 0% 处的颜色*/}, {offset: 1, color: '#daf163' /* 100% 处的颜色*/}]
            },
            shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 1, y2: 1,
                colorStops: [{offset: 0, color: '#fb7d64' /* 0% 处的颜色*/}, {offset: 1, color: '#f93b67' /* 100% 处的颜色*/}]
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 1, y2: 1,
                colorStops: [{offset: 0, color: '#26a8e6' /* 0% 处的颜色*/}, {offset: 1, color: '#1ec5af' /* 100% 处的颜色*/}]
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 1, y2: 1,
                colorStops: [{offset: 0, color: '#e251b6' /* 0% 处的颜色*/}, {offset: 1, color: '#b72bee' /* 100% 处的颜色*/}]
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 1, y2: 1,
                colorStops: [{offset: 0, color: '#ffb199' /* 0% 处的颜色*/}, {offset: 1, color: '#ff0844' /* 100% 处的颜色*/}]
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    },
    {
        normal: {
            color: {
                type: 'linear', x: 0, y: 0, x2: 1, y2: 1,
                colorStops: [{offset: 0, color: '#ff6c69' /* 0% 处的颜色*/}, {offset: 1, color: '#eb9c60' /* 100% 处的颜色*/}]
            }, shadowBlur: 200, shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
    }
];
