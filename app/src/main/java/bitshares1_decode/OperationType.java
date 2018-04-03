package bitshares1_decode;

import java.io.Serializable;

/**
 * Enum type used to keep track of all the operation types and their corresponding ids.
 *
 * <a href="https://bitshares.org/doxygen/operations_8hpp_source.html">Source</a>
 *
 * Created by nelson on 11/6/16.
 */
public enum OperationType implements IValueEnum{
	null_op_type(0),
	withdraw_op_type(1),
	deposit_op_type(2),
	register_account_op_type(3),
	update_account_op_type(4),           // VIRTUAL
	withdraw_pay_op_type(5),
	create_asset_op_type(6),
	update_asset_op_type(7),
	issue_asset_op_type(8),
	reserved_op_2_type(11),
	reserved_op_3_type(17),
	define_slate_op_type(18),
	reserved_op_4_type(21),
	reserved_op_5_type(22),
	release_escrow_op_type(23),
	update_signing_key_op_type(24),
	update_balance_vote_op_type(27),
	update_asset_ext_op_type(30),
	imessage_memo_op_type(66),
	contract_info_op_type(68),
	contract_register_op_type(70),
	contract_upgrade_op_type(71),
	contract_destroy_op_type(72),
	contract_call_op_type(73),
	transfer_contract_op_type(74),
	withdraw_contract_op_type(80),
	deposit_contract_op_type(82),
	balances_withdraw_op_type(88),
	transaction_op_type(90),
	storage_op_type(91),
	event_op_type(100),
	on_destroy_op_type(108),
	on_upgrade_op_type(109),
	on_call_success_op_type(110),
	on_get_contract_fee_operation(120);
	
	
	
	private Byte value;
	OperationType() {}
	private OperationType(int value) {
        this.value = (byte)value;
    }
	private OperationType(byte value) {
        this.value = value;
    }
	public void setValue(byte value) {
		this.value = value;
	}
	
	public byte getValue() {
		return value;
	}

	
}
