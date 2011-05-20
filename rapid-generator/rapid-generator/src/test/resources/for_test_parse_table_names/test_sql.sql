    SELECT
        count(*) totalCount,    (case
            when ( TRANS_AMOUNT > 0 ) then TRANS_AMOUNT
        end) as incomeAmount ,    count((case
            when ( TRANS_AMOUNT > 0 ) then TRANS_AMOUNT
        end)) as incomeCount ,    (case
            when ( TRANS_AMOUNT < 0 ) then TRANS_AMOUNT
        end) as outcomeAmount ,    count((case
            when ( TRANS_AMOUNT < 0 ) then TRANS_AMOUNT
        end)) as outcomeCount
    FROM
        mcenter_mini_account_log  MAL
    LEFT JOIN
        mcenter_air_ext MAE
            on MAL.biz_uniq_no = MAE.trade_no
            AND MAE.owner IN  (
                ?
            )
    LEFT JOIN
        mcenter_creditpay MC
            on MAL.biz_uniq_no = MC.trade_no
            AND MC.owner IN  (
                ?
            )
    WHERE
        MAL.owner IN  (
            ?
        )
        AND     MAL.MINI_TRANS_LOG_ID = ?
        AND     MAL.BIZ_UNIQ_NO IN  (
            ?
        )
        AND     MAL.BIZ_UNIQ_OUT_NO = ?
        and     MAL.ACCOUNT_NO IN  (
            ?
        )
        and     MAL.ISSUER_SUB_ID IN  (
            ?
        )
        AND     MAL.PARTNER_ID IN  (
            ?
        )
        and MAL.PARTNER_ID is null
        and      MAL.trans_dt >= ?
        and      MAL.trans_dt < ?
        and        sub_trans_code in (
            '500481','600181'
        )
        and        sub_trans_code in (
            '400383','600182'
        )
        and        sub_trans_code in (
            '500482','600183'
        )
        and        sub_trans_code in (
            '400381'
        )
        and        sub_trans_code in (
            '400382'
        )
        and        sub_trans_code in (
            '100181'
        )
        and        sub_trans_code in (
            '500483'
        )
        and        sub_trans_code in (
            '600184'
        )
        and        sub_trans_code not in (
            '500481','600181','400383','600182','500482','600183','400381','400382','100181','500483','

        )
        and     MAE.pnr = ?
        and      MAE.TICKET_NO_END >= ?
        AND MAE.TICKET_NO_START <= ?
        AND length(MAE.TICKET_NO_END) >= length(?)
        AND length(MAE.TICKET_NO_START) <= length(?)     1