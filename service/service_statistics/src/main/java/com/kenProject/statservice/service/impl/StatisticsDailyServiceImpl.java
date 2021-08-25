package com.kenProject.statservice.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kenProject.commonutils.Result;
import com.kenProject.statservice.client.UcenterClient;
import com.kenProject.statservice.entity.StatisticsDaily;
import com.kenProject.statservice.mapper.StatisticsDailyMapper;
import com.kenProject.statservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-08-23
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public void registerCount(String day) {

      //添加记录之前删除表相同日期的数据
      QueryWrapper<StatisticsDaily> wrapper=new QueryWrapper<>();
      wrapper.eq("date_calculated",day);
      baseMapper.delete(wrapper);


      //远程调用得到某一天的注册人数
        Result registerR = ucenterClient.countRegister(day);
        Integer countRegister = (Integer) registerR.getData().get("countRegister");

      //把获取的数据添加到数据库，统计分析表
        StatisticsDaily sta=new StatisticsDaily();
        sta.setRegisterNum(countRegister);//注册人数
        sta.setDateCalculated(day);//统计日期


        sta.setVideoViewNum(RandomUtils.nextInt(100, 200));
        sta.setLoginNum(RandomUtils.nextInt(100, 200));
        sta.setCourseNum(RandomUtils.nextInt(100, 200));

        baseMapper.insert(sta);

    }

    //图表显示，返回两部分数据，日期json数组，数量json数组
    @Override
    public Map<String,Object> getShowData(String type, String begin, String end) {
        //根据条件查询
        QueryWrapper<StatisticsDaily> wrapper=new QueryWrapper<>();
        wrapper.between("date_calculated",begin,end);
        wrapper.select("date_calculated",type);
        List<StatisticsDaily> staList = baseMapper.selectList(wrapper);


        //因为返回有两部分数据：日期和日期对应数量
        //前端要求数组json结构，对于后端java代码是list集合
        //创建两个list集合，一个日期list，一个数量list
        List<String>  date_calculateList=new ArrayList<>();
        List<Integer>  numDataList=new ArrayList<>();


        //遍历查询所有list集合,进行封装
        for (int i = 0; i < staList.size() ; i++) {
            StatisticsDaily daily = staList.get(i);
            //封装日期list集合
            date_calculateList.add(daily.getDateCalculated());
            //封装对应数量
            switch (type){
                case "login_num":
                    numDataList.add(daily.getLoginNum());
                    break;
                case "register_num":
                    numDataList.add(daily.getRegisterNum());
                    break;
                case "video_view_num":
                    numDataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    numDataList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }

        //把封装之后两个list集合放到map集合，进行返回
        Map<String,Object> map=new HashMap<>();
        map.put("date_calculateList",date_calculateList);
        map.put("numDataList",numDataList);

        return map;
    }
}
