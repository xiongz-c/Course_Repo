package mutiAdapter;

import adapter.StaffModel;

import java.util.Comparator;
import java.util.List;

public class Adapter implements FileOperateInterfaceV2 {

    private FileOperateInterfaceV1 adapt;
    private ManageStaffInterface adapt2;

    Adapter( FileOperateInterfaceV1 adapt ) {
        this.adapt = adapt;
    }

    Adapter( ManageStaffInterface adapt2 ) {
        this.adapt2 = adapt2;
    }

    Adapter( FileOperateInterfaceV1 adapt , ManageStaffInterface adapt2 ) {
        this.adapt = adapt;
        this.adapt2 = adapt2;
    }

    @Override
    public List<StaffModel> readAllStaff() {
        return adapt.readStaffFile( );
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

    @Override
    public void addNewStaff( List<StaffModel> list ) {
        adapt2.addingStaff( list );
    }

    @Override
    public void removeStaffByName( List<StaffModel> list ) {
        adapt2.removeStaff( list );
    }
}
