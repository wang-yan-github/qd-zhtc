<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>小票</title>
</head>
<body>

<div style="width: 383px;height:700px;font-family: 'microsoft yahei', arial;padding: 0;margin: 0;">
    <#--    <img src="data:image/jpeg;base64,${img1}" alt="" style="width: 100%;height: 100%;" />-->
    <ul style="margin:0px;padding: 18px;;border:none;list-style:none;">

            <h2>--------------------------------</h2>
            <li style="line-height: 40px;font-size: 24px;color: #333;">
            <p style="width: 100px;text-align: right;display: inline-block;margin:0px;padding:0px;">票型名称：</p><span>${smallTicket.ticket_name}</span>
        </li>
        <#--<li style="line-height: 40px;font-size: 24px;color: #333;">
            <p style="width: 100px;text-align: right;display: inline-block;margin:0px;padding:0px;">客户类型：</p><span>${printTicketVo.customer_type}</span>
        </li>-->
        <li style="line-height: 40px;font-size: 24px;color: #333;">
            <p style="width: 100px;text-align: right;display: inline-block;margin:0px;padding:0px;">使用人数：</p><span>${smallTicket.number}</span>
        </li>
        <li style="line-height: 40px;font-size: 24px;color: #333;">
            <p style="width: 100px;text-align: right;display: inline-block;margin:0px;padding:0px;">票面价格：</p><span>${smallTicket.par_price}</span>
        </li>
        <li style="line-height: 40px;font-size: 24px;color: #333;">
            <p style="width: 100px;text-align: right;display: inline-block;margin:0px;padding:0px;">销售价：</p><span>${smallTicket.sale_price}</span>
        </li>
        <#--<li style="line-height: 40px;font-size: 24px;color: #333;">-->
            <#--<p style="width: 100px;text-align: right;display: inline-block;margin:0px;padding:0px;">原价格：</p><span>${t.price}</span>-->
        <#--</li>-->


        <#--<li style="line-height: 40px;font-size: 24px;color: #333;">
            <p style="width: 100px;text-align: right;display: inline-block;margin:0px;padding:0px;">发票金额：</p><span>${printTicketVo.invoice_price}</span>
        </li>-->
        <#--<li style="line-height: 40px;font-size: 24px;color: #333;">
            <p style="width: 100px;text-align: right;display: inline-block;margin:0px;padding:0px;">有效期模式：</p><span>${printTicketVo.validate_type}</span>
        </li>
        <li style="line-height: 40px;font-size: 24px;color: #333;">
            <p style="width: 100px;text-align: right;display: inline-block;margin:0px;padding:0px;">截止有效期：</p><span>${printTicketVo.validate_date}</span>
        </li>-->
        <li style="line-height: 40px;font-size: 24px;color: #333;">
            <p style="width: 100px;text-align: right;display: inline-block;margin:0px;padding:0px;">总价：</p><span>${smallTicket.total}</span>
        </li>
        <li style="line-height: 40px;font-size: 24px;color: #333;">
            <p style="width: 100px;text-align: right;display: inline-block;margin:0px;padding:0px;">日期：</p><span>${smallTicket.time}</span>
        </li>
    </ul>

    <div style="margin:24px 0 24px 66px;padding: 0;width: 152px;height: 152px;">
        <img src="data:image/jpeg;base64,${img}" alt="" style="width: 152px;height: 152px;display: inline-block;padding: 0;margin: 0;" />
    </div>

</div>


<#--<span>${msg}</span>-->
</body>
</html>