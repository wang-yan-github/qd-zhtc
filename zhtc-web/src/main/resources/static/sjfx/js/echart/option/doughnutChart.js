var percentOrange = 0.9;
var doughnutChartOrangeOption = {
    title: {
        text: '橙色',
        x: 'center',
        y: 'bottom',
        textStyle: {color: "white"}
    },
    series: [
        {
            type: 'pie',
            radius: [70, 90],
            itemStyle: {
                normal: {
                    label: {
                        formatter: function () {
                            return percentOrange * 100 + '%'
                        },
                        textStyle: {color: "orange",fontSize:20}
                    }
                },
            },
            data: [
                {
                    value: percentOrange * 100,
                    itemStyle: {
                        normal: {
                            color: {type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                                colorStops: [{
                                    offset: 0, color: 'orange' // 0% 处的颜色
                                }, {
                                    offset: 1, color: 'red' // 100% 处的颜色
                                }],
                            },
                            label: {
                                position: 'center',
                                formatter: '{b}'
                            }
                        }
                    }
                },
                {
                    value: 100 - percentOrange * 100,
                    itemStyle: {
                        normal: {
                            color: 'rgba(255,255,255,0.1)',
                            label: {
                                position: 'center'
                            }
                        }
                    }
                },
            ]
        },
    ]
};

var percentPink= 0.81;
var doughnutChartPinkOption = {
    title: {
        text: '粉色',
        x: 'center',
        y: 'bottom',
        textStyle: {color: "white"}
    },
    series: [
        {
            type: 'pie',
            radius: [70, 90],
            itemStyle: {
                normal: {
                    label: {
                        formatter: function () {
                            return percentPink * 100 + '%'
                        },
                        textStyle: {color: "#f93b67",fontSize:20}
                    }
                },
            },
            data: [
                {
                    value: percentPink * 100,
                    itemStyle: {
                        normal: {
                            color: {type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                                colorStops: [{
                                    offset: 0, color: '#fb7d64' // 0% 处的颜色
                                }, {
                                    offset: 1, color: '#f93b67' // 100% 处的颜色
                                }],
                            },
                            label: {
                                position: 'center',
                                formatter: '{b}'
                            }
                        }
                    }
                },
                {
                    value: 100 - percentPink * 100,
                    itemStyle: {
                        normal: {
                            color: 'rgba(255,255,255,0.1)',
                            label: {
                                position: 'center'
                            }
                        }
                    }
                },
            ]
        },
    ]
};

var percentGreen= 0.68;
var doughnutChartGreenOption = {
    title: {
        text: '绿色',
        x: 'center',
        y: 'bottom',
        textStyle: {color: "white"}
    },
    series: [
        {
            type: 'pie',
            radius: [70, 90],
            itemStyle: {
                normal: {
                    label: {
                        formatter: function () {
                            return percentGreen * 100 + '%'
                        },
                        textStyle: {color: "#1bb1a1",fontSize:20}
                    }
                },
            },
            data: [
                {
                    value: percentGreen * 100,
                    itemStyle: {
                        normal: {
                            color: {type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                                colorStops: [{
                                    offset: 0, color: '#80da9a' // 0% 处的颜色
                                }, {
                                    offset: 1, color: '#1bb1a1' // 100% 处的颜色
                                }],
                            },
                            label: {
                                position: 'center',
                                formatter: '{b}'
                            }
                        }
                    }
                },
                {
                    value: 100 - percentGreen * 100,
                    itemStyle: {
                        normal: {
                            color: 'rgba(255,255,255,0.1)',
                            label: {
                                position: 'center'
                            }
                        }
                    }
                },
            ]
        },
    ]
};

var percentPurple= 0.51;
var doughnutChartPurpleOption = {
    title: {
        text: '紫色',
        x: 'center',
        y: 'bottom',
        textStyle: {color: "white"}
    },
    series: [
        {
            type: 'pie',
            radius: [70, 90],
            itemStyle: {
                normal: {
                    label: {
                        formatter: function () {
                            return percentPurple * 100 + '%'
                        },
                        textStyle: {color: "#b72bee",fontSize:20}
                    }
                },
            },
            data: [
                {
                    value: percentPurple * 100,
                    itemStyle: {
                        normal: {
                            color: {type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                                colorStops: [{
                                    offset: 0, color: '#e251b6' // 0% 处的颜色
                                }, {
                                    offset: 1, color: '#b72bee' // 100% 处的颜色
                                }],
                            },
                            label: {
                                position: 'center',
                                formatter: '{b}'
                            }
                        }
                    }
                },
                {
                    value: 100 - percentPurple * 100,
                    itemStyle: {
                        normal: {
                            color: 'rgba(255,255,255,0.1)',
                            label: {
                                position: 'center'
                            }
                        }
                    }
                },
            ]
        },
    ]
};

var percentBlue= 0.47;
var doughnutChartBlueOption = {
    title: {
        text: '蓝色',
        x: 'center',
        y: 'bottom',
        textStyle: {color: "white"}
    },
    series: [
        {
            type: 'pie',
            radius: [70, 90],
            itemStyle: {
                normal: {
                    label: {
                        formatter: function () {
                            return percentBlue * 100 + '%'
                        },
                        textStyle: {color: "#1e66f0",fontSize:20}
                    }
                },
            },
            data: [
                {
                    value: percentBlue * 100,
                    itemStyle: {
                        normal: {
                            color: {type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                                colorStops: [{
                                    offset: 0, color: '#1ad1fb' // 0% 处的颜色
                                }, {
                                    offset: 1, color: '#1e66f0' // 100% 处的颜色
                                }],
                            },
                            label: {
                                position: 'center',
                                formatter: '{b}'
                            }
                        }
                    }
                },
                {
                    value: 100 - percentBlue * 100,
                    itemStyle: {
                        normal: {
                            color: 'rgba(255,255,255,0.1)',
                            label: {
                                position: 'center'
                            }
                        }
                    }
                },
            ]
        },
    ]
};

var percentOther= 0.21;
var doughnutChartOtherOption = {
    title: {
        text: '其他',
        x: 'center',
        y: 'bottom',
        textStyle: {color: "white"}
    },
    series: [
        {
            type: 'pie',
            radius: [70, 90],
            itemStyle: {
                normal: {
                    label: {
                        formatter: function () {
                            return percentOther * 100 + '%'
                        },
                        textStyle: {color: "#ff00b6",fontSize:20}
                    }
                },
            },
            data: [
                {
                    value: percentOther * 100,
                    itemStyle: {
                        normal: {
                            color: {type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
                                colorStops: [{
                                    offset: 0, color: '#ffffb6' // 0% 处的颜色
                                }, {
                                    offset: 1, color: '#ff00b6' // 100% 处的颜色
                                }],
                            },
                            label: {
                                position: 'center',
                                formatter: '{b}'
                            }
                        }
                    }
                },
                {
                    value: 100 - percentOther * 100,
                    itemStyle: {
                        normal: {
                            color: 'rgba(255,255,255,0.1)',
                            label: {
                                position: 'center'
                            }
                        }
                    }
                },
            ]
        },
    ]
};