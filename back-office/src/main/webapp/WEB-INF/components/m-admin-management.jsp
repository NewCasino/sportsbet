<div id="m-admin" class="m-group collapsed">
    <div class="m-title" title="Account">
        <a class="icon"></a>
        <span>
            Admin Management
        </span>
    </div>
    <ul style="display: none; ">
        <li class="profiles">
            <a href="/backoffice/web/profile" target="iContent">
                Edit Profile
            </a>
        </li>
        <auth:authorization anyPermissions="EditRolePermission,ListRolePermission">
            <li class="role-permission">
                <a href="/backoffice/web/role-permission" target="iContent">
                    Role Management
                </a>
            </li>
        </auth:authorization>
        <auth:authorization anyPermissions="EditAdminUser,ListAdminUser">
        <li class="admin">
            <a href="/backoffice/web/admin-manager" target="iContent">
                Admin User Management
            </a>
        </li>
        </auth:authorization>

    </ul>
</div>