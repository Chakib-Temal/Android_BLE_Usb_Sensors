package com.example.bluetoothtest.Models.Usb.medtronic640.message;


import com.example.bluetoothtest.Models.Usb.medtronic640.exception.ChecksumException;

/**
 * Created by lgoedhart on 26/03/2016.
 */
public class ContourNextLinkCommandMessage extends ContourNextLinkRequestMessage<ContourNextLinkCommandResponse> {
    public ContourNextLinkCommandMessage(ASCII command) {
        super(new byte[]{command.getValue()});
    }

    public ContourNextLinkCommandMessage(byte command) {
        super(new byte[]{command});
    }

    public ContourNextLinkCommandMessage(String command) {
        super(command.getBytes());
    }

    @Override
    protected ContourNextLinkCommandResponse getResponse(byte[] payload) throws ChecksumException {
        return new ContourNextLinkCommandResponse(payload);
    }

}
