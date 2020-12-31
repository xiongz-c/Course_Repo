package adapter;

import java.util.Comparator;
import java.util.List;

public class Adapter implements FileOperateInterfaceV2{

    private final FileOperateInterfaceV1 adapt;

    Adapter(FileOperateInterfaceV1 adapt){
        this.adapt = adapt;
    }


    @Override
    public List<StaffModel> readAllStaff() {
        return adapt.readStaffFile();
    }

    @Override
    public void listAllStaff( List<StaffModel> list ) {
        adapt.printStaffFile( list );
    }

    @Override
    public void writeByName( List<StaffModel> list ) {
        list.sort( Comparator.comparing( StaffModel::getName ) );
        adapt.writeStaffFile( list );
    }

    @Override
    public void writeByRoom( List<StaffModel> list ) {
        list.sort( Comparator.comparing( StaffModel::getRoom ) );
        adapt.writeStaffFile( list );
    }
}
